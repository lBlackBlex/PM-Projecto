<template>
  <div class="column">
    <div class="card">
      <header class="card-header">
        <p class="card-header-title has-text-grey">
          {{ flight.id }}
        </p>
      </header>
      <div class="card-content">
        <div class="content has-text-centered">
          <section class="columns">
            <div class="column">
              <div class="columns">
                <div class="column">
                  <p class="title">
                    {{ flight.from }}
                  </p>
                  <p class="subtitle">
                    {{ flight.etd | time }}
                  </p>
                </div>
                <div class="column">
                  <p class="title">
                    {{ flight.to }}
                  </p>
                  <p class="subtitle">
                    {{ flight.eta | time }}
                  </p>
                </div>
                <div class="column">
                  <p class="title">
                    {{ flightTime }}
                  </p>
                  <p class="subtitle">
                    <code>Estimated flight time</code>
                  </p>
                </div>
              </div>
            </div>
            <div class="column">
              <p class="title">
                $ {{ flight.cost }}
              </p>
              <p class="subtitle">
                <code>90 seats left</code>
              </p>
            </div>
          </section>
        </div>
      </div>
      <footer class="card-footer">
        <a v-for="item in flight.airlines" @click="bookFlight" class="card-footer-item">Book {{ item.name }}</a>
      </footer>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    flight: {
      type: Object,
      required: true
    },
  },
  computed: {
    flightTime() {
      const start = new Date(this.flight.etd)
      const end = new Date(this.flight.eta)
      const hours = ((end.getTime() - start.getTime()) / 1000) / 3600
      return hours + 'h'
    }
  },
  methods: {
    bookFlight() {
      console.log(this.flight)
      let user
      let route
      let ticket
      this.$axios.$post(
        'http://localhost:8080/api/v1/tickets',
        {
          seat: 'AA1'
        },
        {
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `${this.$store.getters.getToken}`
          }
        }).then(res => {
        ticket = res.id
        route = this.flight.id
        this.$axios.$get(
          `http://localhost:8080/api/v1/users/find`,
          {
            headers: {
              'Authorization': `${this.$store.getters.getToken}`
            },
            params: {
              'email': `${this.$store.getters.getUserEmail}`
            }
          }
        ).then(res => {
          user = res.id
          this.$axios.$post(
            `http://localhost:8080/api/v1/tickets/${ticket}`,
            {},
            {
              headers: {
                'Content-Type': 'application/json',
                'Authorization': `${this.$store.getters.getToken}`
              },
              params: {
                'routeId': `${route}`,
                'userId': `${user}`
              }
            })
        })
      })
    }
  }
}
</script>
