<template>
  <div>
    <b-navbar class="navbar header has-shadow is-primary">
      <template #brand>
        <b-navbar-item tag="router-link" :to="{ path: '/' }">
          <img
            src="~assets/buefy.png"
            alt="Buefy"
            height="28"
          >
        </b-navbar-item>
      </template>
      <template #start>
        <b-navbar-item tag="router-link" :to="{ path: '/' }">
          Home
        </b-navbar-item>
        <b-navbar-item tag="router-link" :to="{ path: '/routes' }">
          Travel Information
        </b-navbar-item>
        <b-navbar-dropdown label="Info">
          <b-navbar-item href="#">
            About
          </b-navbar-item>
          <b-navbar-item href="#">
            Contact
          </b-navbar-item>
        </b-navbar-dropdown>
        <b-navbar-item v-if="getUserRank === 'ADMIN'" tag="router-link" :to="{ path: '/admin' }">
          Admin Panel
        </b-navbar-item>
      </template>
      <template #end>
        <b-navbar-item tag="div">
          <div class="buttons">
            <a class="button is-primary" @click="signup" v-if="isLoggedIn === false">
              <strong>Sign up</strong>
            </a>
            <a class="button is-light" @click="login" v-if="isLoggedIn === false">
              Log in
            </a>
            <a class="button is-light" @click="login" v-else>
              Log out
            </a>
          </div>
        </b-navbar-item>
      </template>
    </b-navbar>
    <section class="main-content columns">
      <aside class="column is-2 section">
        <Menu/>
      </aside>
      <div class="container column is-10">
        <nuxt />
      </div>
    </section>
  </div>
</template>

<script>
import Menu from "~/components/Menu";
export default {
  middleware: ['check-auth'],
  components: {
    Menu
  },
  computed: {
    isLoggedIn: function (){
      return this.$store.getters.isUserLoggedIn
    },
    getUserRank: function() {
      return this.$store.getters.getUserRank
    }
  },
  methods: {
    login(){
      if(this.$store.getters.isUserLoggedIn){
        this.$store.dispatch('logoutUser')
        this.$router.push('/')
      }else{
        this.$router.push('login')
      }
    },
    signup(){
      this.$router.push('/register')
    }
  }
}
</script>
