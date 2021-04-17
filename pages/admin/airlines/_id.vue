<template>
  <section class="section">
    <div class="columns is-mobile">
      <section class="column">
        <div class="buttons">
          <b-button
            label="Go Back"
            type="is-primary"
            icon-left="arrow-left"
            @click="goBack"/>
        </div>
        <b-taglist attached>
          <b-tag type="is-dark" size="is-large">
            <b-icon
              icon="pound"
              size="is-small">
            </b-icon>
          </b-tag>
          <b-tag type="is-info" size="is-large">{{ airline.id }}</b-tag>
        </b-taglist>

        <AirlineForm
          :airline="airline"
          :showButton="false"/>
        <br>
        <b-message type="is-info" has-icon>
          Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce id fermentum quam. Proin sagittis, nibh id
          hendrerit imperdiet, elit sapien laoreet elit
        </b-message>

        <div class="buttons">
          <b-button
            :label="checkedRows.length <= 1 ? 'Remove Selected' : 'Remove Selecteds'"
            type="is-danger"
            icon-left="delete"
            :disabled="checkedRows.length===0"
            @click="confirmDelete"/>
        </div>

        <b-tabs>
          <b-tab-item label="Table">
            <b-table
              :data="routes"
              :checked-rows.sync="checkedRows"
              checkbox-position="left"
              checkable>
              <b-table-column field="id" label="Flight Number" v-slot="props">
                {{ props.row.id }}
              </b-table-column>
              <b-table-column field="airline" label="Airline" v-slot="props">
                <b-taglist>
                  <b-tag
                    v-for="item in routeAirlines(props.row.id)"
                    type="is-primary">
                    {{ item }}
                  </b-tag>
                </b-taglist>
              </b-table-column>
              <b-table-column field="cost" label="Cost" numeric v-slot="props">
                {{ props.row.cost }}
              </b-table-column>
              <b-table-column field="etd" label="Depart" v-slot="props">
                {{ props.row.etd | time }}
              </b-table-column>
              <b-table-column field="etd" label="Depart" v-slot="props">
                {{ props.row.eta | time }}
                <br>
                <code>Stop: Los Angeles, CA (LAX)</code>
              </b-table-column>
              <b-table-column field="info" label="Info" v-slot="props">
                {{ props.row.from }} to {{ props.row.to }}
                <br>
                {{ props.row.eta | date }}
              </b-table-column>
            </b-table>
          </b-tab-item>
          <b-tab-item :label="checkedRows.length <= 1 ? 'Selected' : 'Selecteds'">
            <pre>{{ checkedRows }}</pre>
          </b-tab-item>
        </b-tabs>
      </section>
    </div>
  </section>
</template>

<script>
import AirlineForm from "~/components/AirlineForm";

export default {
  components: [
    AirlineForm
  ],
  middleware: ['check-auth', 'auth'],
  created() {
    this.getAirline()
    this.getRoutes()
    this.routesWithAirlines = this.$store.getters.flights
  },
  data() {
    return {
      airline: null,
      routes: [],
      checkedRows: [],
      routesWithAirlines: []
    }
  },
  methods: {
    routeAirlines(routeId) {
      const route = this.routesWithAirlines.find(route => route.id === routeId)
      let airlines = null
      if (route.airlines.length !== 0)
        airlines = route.airlines.map(airline => airline.name)
      return airlines
    },
    getAirline() {
      const airlines = this.$store.getters.airlines
      this.airline = airlines.find(airline => airline.id === this.$route.params.id)
    },
    getRoutes() {
      this.routes = this.airline.routes
    },
    goBack() {
      this.$router.back()
    },
    getName(name) {
      if (this.checkedRows.length > 1)
        name += 's'
      return name
    },
    confirmDelete() {
      const title = 'Deleting ' + this.getName('Route')
      const confirmText = 'Delete ' + this.getName('Route')
      this.$buefy.dialog.confirm({
        title: title,
        message: `Are you sure you want to <b>delete</b> this ${this.getName('Route')}? This action cannot be undone.`,
        confirmText: confirmText,
        type: 'is-danger',
        hasIcon: true,
        onConfirm: () => this.removeRoutes()
      })
    },
    removeRoutes() {
      const removedRoutes = []
      this.checkedRows.map(route => {
        this.$axios.$delete(
          `http://localhost:8080/api/v1/routes/${route.id}/${this.$route.params.id}`,
          {
            headers: {
              'Authorization': `${this.$store.getters.getToken}`
            },
          })
        removedRoutes.push(route.id)
      })
      this.routes = this.routes.filter(e => !removedRoutes.includes(e.id))
      this.checkedRows = []
      this.$buefy.toast.open({
        message: this.getName('Route') + ' deleted!',
        type: 'is-danger'
      })
    }
  }
}
</script>
