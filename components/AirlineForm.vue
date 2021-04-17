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
      label="Name"
      label-position="on-border"
      :type="input.type"
      :custom-class="input.customClass"
      :message="input.message">
      <b-input required v-model="airline.name"></b-input>
    </b-field>
    <div class="buttons">
      <b-button
        v-if="showButton"
        label="Create"
        type="is-success"
        icon-left="plus-thick"
        @click="create"/>
      <b-button
        v-else
        label="Update Airline"
        type="is-primary"
        icon-left="pencil"
        @click="update"/>
    </div>
  </section>
</template>

<script>
export default {
  props: {
    airline: {
      type: Object,
      required: true
    },
    showButton: {
      type: Boolean,
      required: true
    }
  },
  data() {
    return {
      input: {
        type: '',
        customClass: '',
        message: ''
      }
    }
  },
  methods: {
    goBack(){
      this.$router.back()
    },
    errorNotification(){
      this.$buefy.notification.open({
        duration: 2500,
        message: `The field <b>name</b> can not be empty`,
        type: 'is-danger',
        hasIcon: true
      })
    },
    create() {
      if (this.airline.name) {
        this.$axios.$post(
          'http://localhost:8080/api/v1/airlines',
          {
            name: this.airline.name
          },
          {
            headers: {
              'Content-Type': 'application/json',
              'Authorization': `${this.$store.getters.getToken}`
            }
          }).then(() => {
          this.$router.back()
          this.$buefy.toast.open({
            message: 'Airline created!',
            type: 'is-success'
          })
        }).catch(() => {
          this.input.type = 'is-danger'
          this.input.customClass = 'has-text-danger'
          this.input.message = 'Name already in use'
          this.$buefy.notification.open({
            duration: 3000,
            message: `Airline name: <b>${this.airline.name}</b> is already in use`,
            type: 'is-warning',
            hasIcon: true
          })
        })
      } else {
        this.errorNotification()
      }
    },
    update() {
      if(this.airline.name.length === 0){
        this.errorNotification()
        return
      }
      this.$axios.$put(
        `http://localhost:8080/api/v1/airlines/${this.$route.params.id}`,
        {},
        {
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `${this.$store.getters.getToken}`
          },
          params: {
            'name': `${this.airline.name}`
          }
        }).then(() => {
        this.$buefy.toast.open({
          message: 'Airline name updated!',
          type: 'is-success'
        })
        this.input.type = ''
        this.input.customClass = ''
        this.input.message = ''
      }).catch(() => {
        this.input.type = 'is-danger'
        this.input.customClass = 'has-text-danger'
        this.input.message = 'Name already in use'
        this.$buefy.notification.open({
          duration: 3000,
          message: `Airline name: <b>${this.airline.name}</b> is already in use`,
          type: 'is-warning',
          hasIcon: true
        })
      })
      console.log('update ' + this.airline.name)
    },
  }
}
</script>
