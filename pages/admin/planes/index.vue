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
              :data="planes"
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
              <b-table-column field="routes" label="Routes" v-slot="props">
                <b-taglist>
                  <b-tag
                    v-if="planeRoutes(props.row) !== null"
                    v-for="item in planeRoutes(props.row)"
                    type="is-primary">
                    {{ item }}
                  </b-tag>
                  <b-tag v-if="planeRoutes(props.row) === null" type="is-light">No Routes</b-tag>
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
import Table from "~/components/Table";

export default {
  middleware: ['check-auth', 'auth', 'load-data'],
  components: {
    Table
  },
  data() {
    return {
      checkedRows: [],
      planes: this.$store.getters.planes,
    }
  },
  methods: {
    planePilots(plane) {
      let pilots = null
      if (plane.pilots.length !== 0)
        pilots = plane.pilots.map(pilot => `${pilot.name} ${pilot.last_name}`)
      return pilots
    },
    planeRoutes(plane) {
      console.log('una')
      console.log(plane)
      let routes = null
      // const planeRoutes = this.$store.getters.users.filter(user => user.role === 'PILOT' && user.id === pilot.id)[0].planes
      // console.log(pilotPlanes)
      routes = plane.routes.map(route => `${route.from} ${route.to}`)
      return routes
    },
    confirmDelete() {
      let entity = 'Plane'
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
        onConfirm: () => this.removePlane(entity)
      })
    },
    edit() {
      this.$router.push(`planes/${this.checkedRows[0].id}`)
    },
    create() {
      this.$router.push('planes/create')
    },
    removePlane(entity) {
      const removedPlanes = []
      this.checkedRows.map(plane => {
        this.$axios.$delete(
          `http://localhost:8080/api/v1/planes/${plane.id}`,
          {
            headers: {
              'Authorization': `${this.$store.getters.getToken}`
            }
          })
        removedPlanes.push(plane.id)
      })
      this.planes = this.planes.filter(e => !removedPlanes.includes(e.id))
      this.checkedRows = []
      this.$buefy.toast.open({
        message: entity + ' deleted!',
        type: 'is-danger'
      })
    }
  }
}
</script>

