<template>
  <section>
    <div class="buttons">
      <b-button
        v-if="showButton"
        label="Go Back"
        type="is-primary"
        icon-left="arrow-left"
        @click="goBack"/>
    </div>
    <b-field
      label="Date of Flight"
      label-position="on-border"
      :type="inputs.date.type"
      :custom-class="inputs.date.customClass"
      message='Format yyyy-mm-dd'>
      <b-input required required v-model="route.date"></b-input>
    </b-field>
    <b-field
      label="From"
      label-position="on-border">
      <b-input required v-model="route.from"></b-input>
    </b-field>
    <b-field
      label="To"
      label-position="on-border">
      <b-input required required v-model="route.to"></b-input>
    </b-field>
    <b-field
      label="Departure"
      label-position="on-border"
      :type="inputs.etd.type"
      :custom-class="inputs.etd.customClass"
      message='Format yyyy-mm-dd hh:ss'>
      <b-input required required v-model="route.etd"></b-input>
    </b-field>
    <b-field
      label="Arrival"
      label-position="on-border"
      :type="inputs.eta.type"
      :custom-class="inputs.eta.customClass"
      message='Format yyyy-mm-dd hh:ss'>
      <b-input required required v-model="route.eta"></b-input>
    </b-field>
    <b-field
      label="Cost"
      label-position="on-border"
      :type="inputs.cost.type"
      :custom-class="inputs.cost.customClass">
      <b-input required v-model="route.cost"></b-input>
    </b-field>
    <b-field v-if="!showButton" label="Assign airline" label-position="on-border">
      <b-autocomplete
        v-model="airlineToAdd"
        :open-on-focus="true"
        :data="filteredDataObj"
        field="name"
        :clearable="true">
      </b-autocomplete>
    </b-field>
    <b-field v-if="!showButton" label="Assign Plane" label-position="on-border">
      <b-autocomplete
        :disabled="disableAssign"
        v-model="planeToAdd"
        :open-on-focus="true"
        :data="filteredDataObj2"
        field="aircraft_code"
        :clearable="true">
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
        label="Update Airline"
        type="is-primary"
        icon-left="pencil"
        @click="update"/>
      <b-button
        label="Assign Airline"
        type="is-info"
        icon-left="playlist-plus"
        @click="assignAirline()"/>
      <b-button
        label="Assign Plane"
        type="is-info"
        icon-left="playlist-plus"
        @click="assignPlane()"/>
    </div>
  </section>
</template>

<script>
export default {
  props: {
    route: {
      type: Object,
      required: true
    },
    showButton: {
      type: Boolean,
      required: true
    }
  },
  mounted() {
    this.formatDatetime()
  },
  data() {
    return {
      child: this.showButton,
      allAirlines: this.$store.getters.airlines,
      airlineToAdd: '',
      allPlanes: this.$store.getters.planes,
      planeToAdd: '',
      disableAssign: false,
      message: '',
      inputs: {
        etd: {
          type: '',
          customClass: '',
        },
        eta: {
          type: '',
          customClass: '',
        },
        date: {
          type: '',
          customClass: '',
        },
        cost: {
          type: '',
          customClass: '',
        },
      }
    }
  },
  computed: {
    filteredDataObj() {
      return this.allAirlines.filter(option => {
        return (
          option.name
        )
      })
    },
    filteredDataObj2() {
      return this.allPlanes.filter(option => {
        return (
          option.aircraft_code
        )
      })
    }
  },
  created() {
    this.disablePlaneAssign()
  },
  methods: {
    disablePlaneAssign(){
      if (this.route.plane !== null) {
        this.disableAssign = true
        if(this.route.plane)
          this.planeToAdd = this.route.plane.aircraft_code
      }
    },
    enablePlaneAssign(){
      this.disableAssign = false
      this.planeToAdd = ''
    },
    goBack() {
      this.$router.back()
    },
    formatDatetime() {
      if (!this.child) {
        this.route.etd = this.$root.$options.filters.datetime(this.route.etd)
        this.route.eta = this.$root.$options.filters.datetime(this.route.eta)
      }
    },
    create() {
      if (!this.$moment(this.route.date, 'YYYY-MM-DD', true).isValid()) {
        this.inputs.date.type = 'is-danger'
        this.inputs.date.customClass = 'has-text-danger'
      }
      if (!this.$moment(this.route.etd, 'YYYY-MM-DD HH:SS', true).isValid()) {
        this.inputs.etd.type = 'is-danger'
        this.inputs.etd.customClass = 'has-text-danger'
        return
      }
      if (!this.$moment(this.route.eta, 'YYYY-MM-DD HH:SS', true).isValid()) {
        this.inputs.eta.type = 'is-danger'
        this.inputs.eta.customClass = 'has-text-danger'
        return
      }
      if (isNaN(this.route.cost)) {
        this.inputs.cost.type = 'is-danger'
        this.inputs.cost.customClass = 'has-text-danger'
        return
      }

      this.route.date = new Date(this.route.date)
      this.route.etd = new Date(this.route.etd).getTime()
      this.route.eta = new Date(this.route.eta).getTime()

      this.$axios.$post(
        'http://localhost:8080/api/v1/routes',
        JSON.stringify(this.route),
        {
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `${this.$store.getters.getToken}`
          }
        }
      ).then(() => {
        this.$router.back()
        this.$buefy.toast.open({
          message: 'Route created!',
          type: 'is-success'
        })
      })
    },
    update() {
      if (!this.$moment(this.route.date, 'YYYY-MM-DD', true).isValid()) {
        this.inputs.date.type = 'is-danger'
        this.inputs.date.customClass = 'has-text-danger'
      }
      if (!this.$moment(this.route.etd, 'YYYY-MM-DD HH:SS', true).isValid()) {
        this.inputs.etd.type = 'is-danger'
        this.inputs.etd.customClass = 'has-text-danger'
        return
      }
      if (!this.$moment(this.route.eta, 'YYYY-MM-DD HH:SS', true).isValid()) {
        this.inputs.eta.type = 'is-danger'
        this.inputs.eta.customClass = 'has-text-danger'
        return
      }
      if (isNaN(this.route.cost)) {
        this.inputs.cost.type = 'is-danger'
        this.inputs.cost.customClass = 'has-text-danger'
        return
      }
      if (this.route.from.length === 0) return
      if (this.route.to.length === 0) return

      this.$axios.$put(
        `http://localhost:8080/api/v1/routes/${this.$route.params.id}`,
        {},
        {
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `${this.$store.getters.getToken}`
          },
          //todo etd, eta formato
          params: {
            'date': `${this.route.date}`,
            'from': `${this.route.from}`,
            'to': `${this.route.to}`,
            'etd': `${this.route.etd}:00`,
            'eta': `${this.route.eta}:00`,
            'cost': `${this.route.cost}`,
          }
        }).then(() => {
        this.$buefy.toast.open({
          message: 'Route updated!',
          type: 'is-success'
        })
      }).catch(err => {
        console.log(err)
      })

      this.inputs.etd.type = ''
      this.inputs.etd.customClass = ''
      this.inputs.eta.type = ''
      this.inputs.eta.customClass = ''
      this.inputs.date.type = ''
      this.inputs.date.customClass = ''
      this.inputs.cost.type = ''
      this.inputs.cost.customClass = ''
      console.log('update')
    },
    assignAirline() {
      if (this.airlineToAdd.length === 0) {
        this.$buefy.notification.open({
          duration: 2000,
          message: `Select an <b>available airline</b> first`,
          type: 'is-danger',
          hasIcon: true
        })
      } else {
        const selected = this.allAirlines.find(obj => {
          return obj.name === this.airlineToAdd
        })
        this.$axios.$post(
          `http://localhost:8080/api/v1/routes/${this.$route.params.id}`,
          {},
          {
            headers: {
              'Authorization': `${this.$store.getters.getToken}`
            },
            params: {
              'airlineId': `${selected.id}`
            }
          }
        ).then(() => {
          this.airlineToAdd = ''
          this.$parent.addAirline(selected)
        })
        console.log('assign ' + this.airlineToAdd)
      }
    },
    assignPlane() {
      if (this.planeToAdd.length === 0) {
        this.$buefy.notification.open({
          duration: 2000,
          message: `Select an <b>available plane</b> first`,
          type: 'is-danger',
          hasIcon: true
        })
      } else {
        const selected = this.allPlanes.find(plane => {
          return plane.aircraft_code === this.planeToAdd
        })
        this.$axios.$post(
          `http://localhost:8080/api/v1/planes/${selected.id}`,
          {},
          {
            headers: {
              'Authorization': `${this.$store.getters.getToken}`
            },
            params: {
              'routeId': `${this.$route.params.id}`
            }
          }
        ).then(() => {
          this.planeToAdd = ''
          this.$parent.addPlane(selected)
        })
        console.log('assign ' + this.planeToAdd)
      }
    }
  }
}
</script>
