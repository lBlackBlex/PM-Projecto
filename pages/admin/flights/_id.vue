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
          <b-tag type="is-info" size="is-large">{{ route.id }}</b-tag>
        </b-taglist>
        <RouteForm
          :route="route"
          :showButton="false"
          ref="route_plane_form"/>
        <br>

        <b-message type="is-info" has-icon>
          Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce id fermentum quam. Proin sagittis, nibh id
          hendrerit imperdiet, elit sapien laoreet elit
        </b-message>
        <Table
          class="column"
          :data="airlines"
          :columns="columns"
          :special="true"
          control="Route,Airline"/>
        <div class="buttons">
          <b-button
            label="Remove Selected"
            type="is-danger"
            icon-left="delete"
            :disabled="checkedRows.length===0"
            @click="confirmDelete"/>
        </div>
        <b-tabs>
          <b-tab-item label="Table">
            <b-table
              :data="plane"
              :checked-rows.sync="checkedRows"
              checkbox-position="left"
              checkable>
              <b-table-column field="id" label="Flight Number" v-slot="props">
                {{ props.row.id }}
              </b-table-column>
              <b-table-column field="aircraft_code" label="Aircraft Code" v-slot="props">
                {{ props.row.aircraft_code }}
              </b-table-column>
              <b-table-column field="pilot" label="Pilots" v-slot="props">
                <b-taglist>
                  <b-tag
                    v-if="planePilots(props.row) !== null"
                    v-for="item in planePilots(props.row)"
                    type="is-primary">
                    {{ item }}
                  </b-tag>
                  <b-tag v-if="planePilots(props.row) === null" type="is-light">No Pilots</b-tag>
                </b-taglist>
              </b-table-column>
              <b-table-column field="capacity" label="Capacity" v-slot="props">
                {{ props.row.capacity }}
              </b-table-column>
              <b-table-column field="model" label="Model" v-slot="props">
                {{ props.row.model }}
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
import RouteForm from "~/components/RouteForm";
import Table from "~/components/Table";

export default {
  //todo que no se asigne un avion que vuele el mismo dia
  middleware: ['check-auth', 'auth'],
  components: [
    RouteForm,
    Table
  ],
  created() {
    this.getRoute()
    this.getAirlines()
    this.getPlane()
  },
  data() {
    return {
      route: null,
      airlines: [],
      plane: [],
      checkedRows: [],
      columns: [
        {
          field: 'id',
          label: 'ID',
        },
        {
          field: 'name',
          label: 'Name',
        },
      ]
    }
  },
  //todo mostrar si se llego al limite de capacidad y bloquear la asignacion de ruta
  methods: {
    goBack() {
      this.$router.back()
    },
    getRoute() {
      const routes = this.$store.getters.flights
      this.route = routes.find(route => route.id === this.$route.params.id)
    },
    getAirlines() {
      this.airlines = this.route.airlines
    },
    getPlane() {
      console.log(this.route.plane)
        this.plane = [this.route.plane]
    },
    planePilots(plane) {
      let pilots = null
      if (plane.pilots.length !== 0)
        pilots = plane.pilots.map(pilot => `${pilot.name} ${pilot.last_name}`)
      return pilots
    },
    getName(name) {
      if (this.checkedRows.length > 1)
        name += 's'
      return name
    },
    confirmDelete() {
      const title = 'Deleting ' + this.getName('Plane')
      const confirmText = 'Delete ' + this.getName('Plane')
      this.$buefy.dialog.confirm({
        title: title,
        message: `Are you sure you want to <b>delete</b> this ${this.getName('Plane')}? This action cannot be undone.`,
        confirmText: confirmText,
        type: 'is-danger',
        hasIcon: true,
        onConfirm: () => this.removeRoutePlane()
      })
    },
    removeRouteAirlines(checkedRows, entity) {
      const removedAirlines = []
      checkedRows.map(airline => {
        this.$axios.$delete(
          `http://localhost:8080/api/v1/routes/${this.$route.params.id}/${airline.id}`,
          {
            headers: {
              'Authorization': `${this.$store.getters.getToken}`
            },
          })
        removedAirlines.push(airline.id)
      })
      console.log(removedAirlines)
      this.airlines = this.airlines.filter(e => !removedAirlines.includes(e.id))
      this.$buefy.toast.open({
        message: entity + ' removed!',
        type: 'is-danger'
      })
    },
    addAirline(airlineToAdd) {
      if (this.airlines.find(airline => airline.id === airlineToAdd.id))
        this.$buefy.notification.open({
          duration: 4000,
          message: `This route already has <b>${airlineToAdd.id}</b> assigned`,
          type: 'is-warning',
          hasIcon: true
        })
      else {
        this.airlines.push(airlineToAdd)
        this.$buefy.toast.open({
          message: 'Airline assigned successfully!',
          type: 'is-success'
        })
      }
    },
    addPlane(planeToAdd) {
      console.log(planeToAdd)
      this.plane = [planeToAdd]
      this.$buefy.toast.open({
        message: 'Plane assigned successfully!',
        type: 'is-success'
      })
    },
    removeRoutePlane() {
      this.$axios.$delete(
        //"{planeId}/{routeId}"
        `http://localhost:8080/api/v1/planes/${this.plane[0].id}/${this.$route.params.id}`,
        {
          headers: {
            'Authorization': `${this.$store.getters.getToken}`
          },
        }).then(() => {
        this.plane = []
        this.checkedRows = []
        this.$refs.route_plane_form.enablePlaneAssign()
        this.$buefy.toast.open({
          message: this.getName('Plane') + ' deleted!',
          type: 'is-danger'
        })
      })
    },
  }
}
</script>
