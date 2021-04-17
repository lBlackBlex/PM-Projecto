<template>
  <section class="section">
    <div class="columns is-mobile">
      <section class="column">
        <div class="buttons">
          <b-button
            label="Edit selected"
            type="is-primary"
            icon-left="pencil"
            :disabled="checkedRows.length===0 || checkedRows.length>1"
            @click="edit"/>
          <b-button
            label="Create new"
            type="is-success"
            icon-left="plus-thick"
            @click="create"/>
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
              :data="flights"
              :checked-rows.sync="checkedRows"
              checkbox-position="left"
              checkable>
              <b-table-column field="id" label="Flight Number" v-slot="props">
                {{ props.row.id }}
              </b-table-column>
              <b-table-column field="airline" label="Airline" v-slot="props">
                <b-taglist>
                  <b-tag
                    v-if="routeAirlines(props.row) !== null"
                    v-for="item in routeAirlines(props.row)"
                    type="is-primary">
                      {{ item }}
                  </b-tag>
                  <b-tag v-if="routeAirlines(props.row) === null" type="is-light">No Airlines</b-tag>
                </b-taglist>
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
export default {
  middleware: ['check-auth', 'auth', 'load-data'],
  data() {
    return {
      checkedRows: [],
      flights: this.$store.getters.flights,
    }
  },
  methods: {
    routeAirlines(route) {
      let airlines = null
      if (route.airlines.length !== 0)
        airlines = route.airlines.map(airline => `${airline.name}`)
      return airlines
    },
    confirmDelete() {
      let entity = 'Flight'
      if (this.checkedRows.length > 1)
        entity += 's'
      const title = 'Deleting ' + entity
      const confirmText = 'Delete ' + entity
      this.$buefy.dialog.confirm({
        title: title,
        message: `Are you sure you want to <b>delete</b> this ${entity.toLocaleLowerCase()}? This action cannot be undone.`,
        confirmText: confirmText,
        type: 'is-danger',
        hasIcon: true,
        onConfirm: () => this.removeFlights(entity)
      })
    },
    edit() {
      this.$router.push(`flights/${this.checkedRows[0].id}`)
    },
    create() {
      this.$router.push('flights/create')
    },
    removeFlights(entity) {
      const removedFlights = []
      this.checkedRows.map(flight => {
        this.$axios.$delete(
          `http://localhost:8080/api/v1/routes/${flight.id}`,
          {
            headers: {
              'Authorization': `${this.$store.getters.getToken}`
            }
          })
        removedFlights.push(flight.id)
      })
      this.flights = this.flights.filter(e => !removedFlights.includes(e.id))
      this.checkedRows = []
      this.$buefy.toast.open({
        message: entity + ' deleted!',
        type: 'is-danger'
      })
    }
  }
}
</script>
