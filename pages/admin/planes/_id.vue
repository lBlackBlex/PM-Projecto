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
          <b-tag type="is-info" size="is-large">{{ plane.id }}</b-tag>
        </b-taglist>
        <PlaneForm
        :plane="plane"
        :showButton="false"
        ref="plane_form"/>
        <br>
        <b-message type="is-info" has-icon>
          Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce id fermentum quam. Proin sagittis, nibh id
          hendrerit imperdiet, elit sapien laoreet elit
        </b-message>
        <div class="buttons">
          <b-button
            :label="routeCheckedRows.length <= 1 ? 'Remove Selected' : 'Remove Selecteds'"
            type="is-danger"
            icon-left="delete"
            :disabled="routeCheckedRows.length===0"
            @click="confirmDelete('Route')"/>
        </div>
        <b-tabs>
          <b-tab-item label="Table">
            <b-table
              :data="routes"
              :checked-rows.sync="routeCheckedRows"
              checkbox-position="left"
              checkable>
              <b-table-column field="id" label="Flight Number" v-slot="props">
                {{ props.row.id }}
              </b-table-column>
              <b-table-column field="airline" label="Airline" v-slot="props">
                <b-taglist>
                  <b-tag
                    v-for="item in routeAirlines(props.row)"
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
          <b-tab-item :label="routeCheckedRows.length <= 1 ? 'Selected' : 'Selecteds'">
            <pre>{{ routeCheckedRows }}</pre>
          </b-tab-item>
        </b-tabs>
        <div class="buttons">
          <b-button
            :label="pilotsCheckedRows.length <= 1 ? 'Remove Selected' : 'Remove Selecteds'"
            type="is-danger"
            icon-left="delete"
            :disabled="pilotsCheckedRows.length===0"
            @click="confirmDelete('Pilot')"/>
        </div>
        <b-tabs>
          <b-tab-item label="Table">
            <b-table
              :data="pilots"
              :checked-rows.sync="pilotsCheckedRows"
              checkbox-position="left"
              checkable>
              <b-table-column field="id" label="ID" v-slot="props">
                {{ props.row.id }}
              </b-table-column>
              <b-table-column field="plane" label="Planes" v-slot="props">
                <b-taglist>
                  <b-tag
                    v-for="item in pilotPlanes(props.row)"
                    type="is-primary">
                    {{ item }}
                  </b-tag>
                </b-taglist>
              </b-table-column>
              <b-table-column field="email" label="Email" v-slot="props">
                {{ props.row.email }}
              </b-table-column>
              <b-table-column field="name" label="Name" v-slot="props">
                {{ props.row.name }}
              </b-table-column>
              <b-table-column field="last_name" label="Last Name" v-slot="props">
                {{ props.row.last_name }}
              </b-table-column>
            </b-table>
          </b-tab-item>
          <b-tab-item :label="pilotsCheckedRows.length <= 1 ? 'Selected' : 'Selecteds'">
<!--            <pre>{{ pilotsCheckedRows }}</pre>-->
          </b-tab-item>
        </b-tabs>
      </section>
    </div>
  </section>
</template>

<script>
import PlaneForm from "~/components/PlaneForm";
import Table from "~/components/Table";
//todo linitar el numero de pilotos a dos

export default {
  components: [
    PlaneForm,
    Table
  ],
  middleware: ['check-auth', 'auth'],
  created() {
    this.getPlane()
    this.getRoutes()
    this.getPilots()
  },
  data() {
    return {
      plane: null,
      routes: [],
      pilots: [],
      routeCheckedRows: [],
      pilotsCheckedRows: []
    }
  },
  methods: {
    routeAirlines(route) {
      let airlines = null
      if (route.airlines.length !== 0)
        airlines = route.airlines.map(airline => airline.name)
      return airlines
    },
    pilotPlanes(pilot){
      let planes = null
      const pilotPlanes = this.$store.getters.users.filter(user => user.role === 'PILOT' && user.id === pilot.id)[0].planes
      console.log(pilotPlanes)
      planes = pilotPlanes.map(plane => plane.aircraft_code)
      return planes
    },
    getPlane() {
      const planes = this.$store.getters.planes
      this.plane = planes.find(plane => plane.id === this.$route.params.id)
    },
    getRoutes() {
      this.routes = this.plane.routes
    },
    getPilots() {
      this.pilots = this.plane.pilots
    },
    goBack() {
      this.$router.back()
    },
    getName(name) {
      if(name === 'Route')
        if (this.routeCheckedRows.length > 1)
          name += 's'
      if(name === 'Pilot')
        if (this.pilotsCheckedRows.length > 1)
          name += 's'
      return name
    },
    confirmDelete(entity) {
      const title = 'Deleting ' + this.getName(entity)
      const confirmText = 'Delete ' + this.getName(entity)
      this.$buefy.dialog.confirm({
        title: title,
        message: `Are you sure you want to <b>delete</b> this ${this.getName(entity)}? This action cannot be undone.`,
        confirmText: confirmText,
        type: 'is-danger',
        hasIcon: true,
        onConfirm: () => {
          if(entity === 'Route')
            this.removeRoutes()
          if(entity === 'Pilot')
            this.removePilots()
        }
      })
    },
    removeRoutes() {
      const removedRoutes = []
      this.routeCheckedRows.map(route => {
        // "{planeId}/{routeId}"
        this.$axios.$delete(
          `http://localhost:8080/api/v1/planes/${this.$route.params.id}/${route.id}`,
          {
            headers: {
              'Authorization': `${this.$store.getters.getToken}`
            },
          })
        removedRoutes.push(route.id)
      })
      this.routes = this.routes.filter(route => !removedRoutes.includes(route.id))
      this.routeCheckedRows = []
      this.$buefy.toast.open({
        message: this.getName('Route') + ' deleted!',
        type: 'is-danger'
      })
    },
    removePilots() {
      const removedPilots = []
      this.pilotsCheckedRows.map(pilot => {
        // "{userId}/{planeId}"
        this.$axios.$delete(
          `http://localhost:8080/api/v1/users/${pilot.id}/${this.$route.params.id}`,
          {
            headers: {
              'Authorization': `${this.$store.getters.getToken}`
            },
          })
        removedPilots.push(pilot.id)
      })

      this.pilots = this.pilots.filter(pilot => !removedPilots.includes(pilot.id))
      this.pilotsCheckedRows = []
      this.$refs.plane_form.disableAssignFromParent(false, this.pilots)


      this.$buefy.toast.open({
        message: this.getName('Pilot') + ' deleted!',
        type: 'is-danger'
      })
    },
    addPilot(pilotToAdd) {
      if (this.pilots.find(pilot => pilot.id === pilotToAdd.id))
        this.$buefy.notification.open({
          duration: 4000,
          message: `This plane already has <b>${pilotToAdd.name} ${pilotToAdd.last_name}</b> assigned`,
          type: 'is-warning',
          hasIcon: true
        })
      else {
        if(!pilotToAdd.planes.find(plane => plane.id === this.$route.params.id)) {
          const planeToAdd = this.plane
          delete planeToAdd.pilots
          pilotToAdd.planes.push(planeToAdd)
        }
        this.pilots.push(pilotToAdd)
        if(this.pilots.length === 2)
          this.$refs.plane_form.disableAssignFromParent(true, this.pilots)
        else
          this.$refs.plane_form.disableAssignFromParent(false, this.pilots)
        this.$buefy.toast.open({
          message: 'Pilot assigned successfully!',
          type: 'is-success'
        })
      }
    }
  }
}
</script>
