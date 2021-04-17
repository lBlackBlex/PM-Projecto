<template>
  <section>
    <div v-if="showButton" class="buttons">
      <b-button
        label="Go Back"
        type="is-primary"
        icon-left="arrow-left"
        @click="goBack"/>
    </div>
    <b-field
      label="Capacity"
      label-position="on-border"
      :type="inputs.capacity.type"
      :custom-class="inputs.capacity.customClass"
      :message="inputs.capacity.message">
      <b-input required v-model="plane.capacity"></b-input>
    </b-field>
    <b-field
      label="Model"
      label-position="on-border"
      :type="inputs.model.type"
      :custom-class="inputs.model.customClass">
      <b-input required v-model="plane.model"></b-input>
    </b-field>
    <b-field
      label="Aircraft Code"
      label-position="on-border"
      :type="inputs.aircraft_code.type"
      :custom-class="inputs.aircraft_code.customClass">
      <b-input required v-model="plane.aircraft_code"></b-input>
    </b-field>
    <b-field v-if="!showButton" label="Assign Pilot" label-position="on-border">
      <b-autocomplete
        :disabled="disableAssign"
        v-model="pilotToAdd"
        :open-on-focus="true"
        :data="filteredDataObj"
        field="fullName"
        :clearable="!disableAssign">
      </b-autocomplete>
    </b-field>
    <div class="buttons" v-if="showButton">
      <b-button
        label="Create"
        type="is-success"
        icon-left="plus-thick"
        @click="create"/>
    </div>
    <div class="buttons" v-else>
      <b-button
        label="Update Boarding Room"
        type="is-primary"
        icon-left="pencil"
        @click="update"/>
      <b-button
        :disabled="disableAssign"
        label="Assign Pilot"
        type="is-info"
        icon-left="playlist-plus"
        @click="assignPilot()"/>
    </div>
  </section>
</template>

<script>
export default {
  props: {
    plane: {
      type: Object,
      required: true
    },
    showButton: {
      type: Boolean,
      required: true
    },
  },
  data() {
    return {
      disableAssign: false,
      pilotToAdd: '',
      allPilots: this.$store.getters.users.filter(user => user.role === 'PILOT'),
      inputs: {
        capacity: {
          type: '',
          customClass: '',
          message: ''
        },
        model: {
          type: '',
          customClass: '',
          message: ''
        },
        aircraft_code: {
          type: '',
          customClass: '',
          message: ''
        }
      }
    }
  },
  computed: {
    filteredDataObj() {
      return this.allPilots.filter(user => user.role === 'PILOT').filter(user => {
        if (user.role === 'PILOT') {
          //todo que le den
          // if (this.plane.pilots.some(pilot => pilot.id === user.id)) return
          user["fullName"] = user.name + ' ' + user.last_name;
          return user
        }
      })
    }

  },
  created() {
    this.disablePilotAssign()
  },
  methods: {
    enablePilotAssign() {
      this.disableAssign = false
      this.pilotToAdd = ''
    },
    disableAssignFromParent(enable ,pilots){
      this.disableAssign = enable

      const pilotsManaged = pilots.filter(pilot => {
        // if(pilot.planes)
          // delete pilot.planes
        return pilot
      })
      console.log(pilotsManaged)
      this.plane.pilots = pilotsManaged
      this.disablePilotAssign()
      if(!enable)
        this.pilotToAdd = ''
    },
    disablePilotAssign() {
      if (this.plane.pilots)
        if (this.plane.pilots.length === 2) {
          this.disableAssign = true
          if (this.plane.pilots) {
            const pilotsName = this.plane.pilots.map(pilot => `${pilot.name} ${pilot.last_name}`)
            for (let i = 0; i < pilotsName.length; i++) {
              if (i === pilotsName.length - 1)
                this.pilotToAdd += ', ' + pilotsName[i]
              else
                this.pilotToAdd += pilotsName[i]
            }
          }
        }
    },
    goBack() {
      this.$router.back()
    },
    errorNotification() {
      this.$buefy.notification.open({
        duration: 3000,
        message: `Plane aircraft code: <b>${this.plane.aircraft_code}</b> is already in use`,
        type: 'is-warning',
        hasIcon: true
      })
    },
    create() {
      if (isNaN(this.plane.capacity)) {
        this.inputs.capacity.type = 'is-danger'
        this.inputs.capacity.customClass = 'has-text-danger'
        this.inputs.capacity.message = 'This field must be a number'
        return
      }
      if (!this.plane.model) {
        this.inputs.model.type = 'is-danger'
        this.inputs.model.customClass = 'has-text-danger'
        return
      }
      if (!this.plane.aircraft_code) {
        this.inputs.aircraft_code.type = 'is-danger'
        this.inputs.aircraft_code.customClass = 'has-text-danger'
        return
      }

      this.$axios.$post(
        'http://localhost:8080/api/v1/planes',
        {
          capacity: this.plane.capacity,
          model: this.plane.model,
          aircraft_code: `${this.plane.aircraft_code.toLocaleUpperCase()}`
        },
        {
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `${this.$store.getters.getToken}`
          }
        }).then(() => {
        this.$router.back()
        this.$buefy.toast.open({
          message: 'Plane created!',
          type: 'is-success'
        })
      }).catch(() => {
        this.inputs.aircraft_code.type = 'is-danger'
        this.inputs.aircraft_code.customClass = 'has-text-danger'
        this.inputs.aircraft_code.message = 'Aircraft code already in use'
        this.errorNotification()
      })
    },
    update() {
      if (isNaN(this.plane.capacity)) {
        this.inputs.capacity.type = 'is-danger'
        this.inputs.capacity.customClass = 'has-text-danger'
        this.inputs.capacity.message = 'This field must be a number'
        return
      }
      if (!this.plane.model) {
        this.inputs.model.type = 'is-danger'
        this.inputs.model.customClass = 'has-text-danger'
        return
      }
      if (!this.plane.aircraft_code) {
        this.inputs.aircraft_code.type = 'is-danger'
        this.inputs.aircraft_code.customClass = 'has-text-danger'
        return
      }

      this.$axios.$put(
        `http://localhost:8080/api/v1/planes/${this.$route.params.id}`,
        {},
        {
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `${this.$store.getters.getToken}`
          },
          params: {
            'capacity': `${this.plane.capacity}`,
            'model': `${this.plane.model}`,
            'aircraft_code': `${this.plane.aircraft_code}`
          }
        }).then(() => {
        this.$buefy.toast.open({
          message: 'Plane updated!',
          type: 'is-success'
        })
        this.inputs.capacity.type = ''
        this.inputs.capacity.customClass = ''
        this.inputs.capacity.message = ''
        this.inputs.model.type = ''
        this.inputs.model.customClass = ''
        this.inputs.model.message = ''
        this.inputs.aircraft_code.type = ''
        this.inputs.aircraft_code.customClass = ''
        this.inputs.aircraft_code.message = ''
      })
    },
    assignPilot() {
      if (this.pilotToAdd !== '') {
        const pilotFullName = this.pilotToAdd.split(' ')
        const selected = this.allPilots.find(pilot => {
          return pilot.name === pilotFullName[0] && pilot.last_name === pilotFullName[1]
        })
        this.$axios.$post(
          `http://localhost:8080/api/v1/users/${selected.id}`,
          {},
          {
            headers: {
              'Authorization': `${this.$store.getters.getToken}`
            },
            params: {
              'planeId': `${this.$route.params.id}`
            }
          }
        )
        this.pilotToAdd = ''
        this.disablePilotAssign()
        this.$parent.addPilot(selected)
      } else {
        this.$buefy.notification.open({
          duration: 2000,
          message: `Select an <b>available pilot</b> first`,
          type: 'is-danger',
          hasIcon: true
        })
      }
    },
  }
}
</script>
