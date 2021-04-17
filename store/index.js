import Vuex from 'vuex'
import Cookies from 'js-cookie'

const createStore = () => {
  return new Vuex.Store({
    state: {
      user: {
        rank: null,
        email: null
      },
      isUserLoggedIn: false,
      token: null,
      flightsFounded: [],
      flights: [],
      airlines: [],
      planes: [],
      boardingRooms: [],
      terminals: [],
      users: [],
    },
    mutations: {
      setUserLoggedIn(state, loggedIn) {
        state.isUserLoggedIn = loggedIn
      },
      setToken(state, jwt) {
        state.token = jwt
      },
      cleatToken(state) {
        state.token = null
      },
      setFightsFounded(state, flights) {
        state.flightsFounded = flights
      },
      setFlights(state, flights) {
        state.flights = flights
      },
      setAirlines(state, airlines) {
        state.airlines = airlines
      },
      setPlanes(state, planes) {
        state.planes = planes
      },
      setTerminals(state, terminals) {
        state.terminals = terminals
      },
      setBoardingRooms(state, boardingRooms) {
        state.boardingRooms = boardingRooms
      },
      setUsers(state, users) {
        state.users = users
      },
      setUserRank(state, rank){
        state.user.rank = rank
      },
      setUserEmail(state, email){
        state.user.email = email
      }
      // setUser(state, email, rank) {
      //   state.user.email = email
      //   state.user.rank = rank
      // },
    },
    actions: {
      async loadFlights(vuexContext) {
        const flights = await this.$axios.$get('http://localhost:8080/api/v1/routes/all')
        vuexContext.commit('setFlights', flights)
      },
      async loadAirlines(vuexContext) {
        const airlines = await this.$axios.$get('http://localhost:8080/api/v1/airlines', {
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `${this.getters.getToken}`
          }
        })
        vuexContext.commit('setAirlines', airlines)
      },
      async loadPlanes(vuexContext) {
        const planes = await this.$axios.$get('http://localhost:8080/api/v1/planes', {
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `${this.getters.getToken}`
          }
        })
        vuexContext.commit('setPlanes', planes)
      },
      async loadBoardingRooms(vuexContext) {
        const boardingRooms = await this.$axios.$get('http://localhost:8080/api/v1/boarding_rooms', {
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `${this.getters.getToken}`
          }
        })
        vuexContext.commit('setBoardingRooms', boardingRooms)
      },
      async loadTerminals(vuexContext) {
        const terminals = await this.$axios.$get('http://localhost:8080/api/v1/terminals', {
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `${this.getters.getToken}`
          }
        })
        vuexContext.commit('setTerminals', terminals)
      },
      async loadUsers(vuexContext) {
        const users = await this.$axios.$get('http://localhost:8080/api/v1/users/all', {
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `${this.getters.getToken}`
          }
        })
        vuexContext.commit('setUsers', users)
      },
      loadAdminData(vuexContext) {
        if (this.getters.getToken != null) {
          vuexContext.dispatch('loadFlights')
          vuexContext.dispatch('loadAirlines')
          vuexContext.dispatch('loadPlanes')
          vuexContext.dispatch('loadBoardingRooms')
          vuexContext.dispatch('loadTerminals')
          vuexContext.dispatch('loadUsers')
        }
      },
      flightsFounded(vuexContext, flights) {
        vuexContext.commit('setFightsFounded', flights)
      },
      setRoutes(vuexContext, routes) {
        vuexContext.commit('setRoutes', routes)
      },
      isUserLoggedIn(vuexContext, loggedIn) {
        vuexContext.commit('setUserLoggedIn', loggedIn)
      },
      logoutUser(vuexContext) {
        vuexContext.commit('setUserLoggedIn', false)
        vuexContext.commit('cleatToken')
        vuexContext.commit('setUserRank', null)
        vuexContext.commit('setUserEmail', null)
        Cookies.remove('jwtToken')
        Cookies.remove('loggedIn')
        Cookies.remove('rank')
        Cookies.remove('email')
        if (process.client) {
          localStorage.removeItem('jwtToken')
          localStorage.removeItem('loggedIn')
          localStorage.removeItem('email')
          localStorage.removeItem('rank')
        }
        this.$router.push('/login')
      },
      loginUser(vuexContext, credentials) {
        this.$axios({
          method: 'post',
          url: 'http://localhost:8080/login',
          data: credentials
        }).then(res => {
          vuexContext.dispatch('saveToken', res.headers.authorization)
          const base64Url = res.headers.authorization.split('.')[1];
          const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
          const jsonPayload = decodeURIComponent(atob(base64).split('').map(function (c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
          }).join(''));

          const userData = JSON.parse(jsonPayload)
          console.log(userData)
          let auth
          for (const o of userData.authorities) {
            if (o.authority.startsWith('ROLE')) {
              auth = o.authority.split('_')[1]
              localStorage.setItem('rank', auth)
              Cookies.set('rank', auth)
            }
          }

          const email = JSON.parse(credentials).username
          localStorage.setItem('email', email)
          Cookies.set('email', email)
          vuexContext.commit('setUserRank', auth)
          vuexContext.commit('setUserEmail', email)
          if (auth !== 'ADMIN')
            this.$router.push('/')
          else {
            this.$router.push('/admin')
          }
        }).catch(err => {
          console.log(err)
        })
      },
      saveToken(vuexContext, token) {
        localStorage.setItem('jwtToken', token)
        localStorage.setItem('loggedIn', true)
        Cookies.set('jwtToken', token)
        Cookies.set('loggedIn', true)
        vuexContext.commit('setToken', token)
        vuexContext.commit('setUserLoggedIn', true)
      },
      initAuth(vuexContext, req) {
        let token
        let loggedIn
        let rank
        let email
        if (req) {
          if (!req.headers.cookie) {
            return
          }
          const jwtCookie = req.headers.cookie
            .split(';')
            .find(c => c.trim().startsWith('jwtToken='))
          if (!jwtCookie) {
            return
          }
          token = jwtCookie.split('=')[1]
          loggedIn = req.headers.cookie
            .split(';')
            .find(c => c.trim().startsWith('loggedIn='))
            .split("=")[1]
          rank = req.headers.cookie
            .split(';')
            .find(c => c.trim().startsWith('rank='))
            .split('=')[1]
          email = req.headers.cookie
            .split(';')
            .find(c => c.trim().startsWith('email='))
            .split('=')[1]
        } else {
          token = localStorage.getItem('jwtToken')
          loggedIn = localStorage.getItem('loggedIn')
          rank = localStorage.getItem('rank')
          email = localStorage.getItem('email')
        }

        if (loggedIn != null) {
          vuexContext.commit('setUserLoggedIn', loggedIn)
          vuexContext.commit('setUserRank', rank)
          vuexContext.commit('setUserEmail', email)
          vuexContext.commit('setToken', token)
        } else {
          vuexContext.commit('setUserLoggedIn', false)
        }
      }
    },
    getters: {
      //puede cambiar por isAuthenticated
      isUserLoggedIn(state) {
        return state.isUserLoggedIn
      },
      getToken(state) {
        return state.token
      },
      flightsFounded(state) {
        return state.flightsFounded
      },
      flights(state) {
        return state.flights
      },
      airlines(state) {
        return state.airlines
      },
      planes(state) {
        return state.planes
      },
      boardingRooms(state) {
        return state.boardingRooms
      },
      terminals(state) {
        return state.terminals
      },
      users(state) {
        return state.users
      },
      getUserRank(state) {
        return state.user.rank
      },
      getUserEmail(state) {
        return state.user.email
      }
    }
  })
}

export default createStore
