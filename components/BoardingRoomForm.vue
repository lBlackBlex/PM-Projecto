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
      :type="inputs.name.type"
      :custom-class="inputs.name.customClass"
      :message="inputs.name.message">
      <b-input required v-model="boardingRoom.name"></b-input>
    </b-field>
    <b-field
      label="Capacity"
      label-position="on-border"
      :type="inputs.capacity.type"
      :custom-class="inputs.capacity.customClass"
      :message="inputs.capacity.message">
      <b-input required v-model="boardingRoom.capacity"></b-input>
    </b-field>
    <b-field v-if="!showButton" label="Assign terminal" label-position="on-border">
      <b-autocomplete
        :disabled="disableAssign"
        v-model="terminalToAdd"
        :open-on-focus="true"
        :data="terminalOptions"
        field="type"
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
        label="Update Boarding Room"
        type="is-primary"
        icon-left="pencil"
        @click="update"/>
      <b-button
        :disabled="disableAssign"
        label="Assign Terminal"
        type="is-info"
        icon-left="playlist-plus"
        @click="assignTerminal"/>
    </div>
  </section>
</template>

<script>
export default {
  props: {
    boardingRoom: {
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
      terminalToAdd: '',
      allTerminals: this.$store.getters.terminals,
      disableAssign: false,
      terminalOptions: ['National', 'International'],
      inputs: {
        name: {
          type: '',
          customClass: '',
          message: ''
        },
        capacity: {
          type: '',
          customClass: '',
          message: ''
        }
      }
    }
  },
  created() {
    this.disableTerminalAssign()
  },
  methods: {
    disableTerminalAssign(){
      if (this.boardingRoom.terminal !== null) {
        this.disableAssign = true
        if(this.boardingRoom.terminal)
          this.terminalToAdd = this.boardingRoom.terminal.type === 1 ? 'National' : 'International'
      }
    },
    enableTerminalAssign(){
      this.disableAssign = false
      this.terminalToAdd = ''
    },
    goBack() {
      this.$router.back()
    },
    create() {
      if (!this.boardingRoom.name) {
        this.inputs.name.type = 'is-danger'
        this.inputs.name.customClass = 'has-text-danger'
        return
      }
      if (!this.boardingRoom.capacity) {
        this.inputs.capacity.type = 'is-danger'
        this.inputs.capacity.customClass = 'has-text-danger'
        return
      }
      if (isNaN(this.boardingRoom.capacity)) {
        this.inputs.capacity.type = 'is-danger'
        this.inputs.capacity.customClass = 'has-text-danger'
        this.inputs.capacity.message = 'This value must be number'
        return
      }
      if(this.boardingRoom.capacity > 32767){
        this.inputs.capacity.type = 'is-danger'
        this.inputs.capacity.customClass = 'has-text-danger'
        this.inputs.capacity.message = 'Boarding room max capacity is 32767'
        return
      }

      this.$axios.$post(
        'http://localhost:8080/api/v1/boarding_rooms',
        {
          name: this.boardingRoom.name,
          capacity: this.boardingRoom.capacity
        },
        {
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `${this.$store.getters.getToken}`
          }
        }).then(() => {
        this.$router.back()
        this.$buefy.toast.open({
          message: 'Boarding room created!',
          type: 'is-success'
        })
      }).catch(() => {
        this.inputs.capacity.type = ''
        this.inputs.capacity.customClass = ''
        this.inputs.capacity.message = ''
        this.inputs.name.type = 'is-danger'
        this.inputs.name.customClass = 'has-text-danger'
        this.inputs.name.message = 'Name already in use'
        this.$buefy.notification.open({
          duration: 3000,
          message: `Boarding room name: <b>${this.boardingRoom.name}</b> is already in use`,
          type: 'is-warning',
          hasIcon: true
        })
      })
    },
    update() {
      if (!this.boardingRoom.name) {
        this.inputs.name.type = 'is-danger'
        this.inputs.name.customClass = 'has-text-danger'
        return
      }
      if (!this.boardingRoom.capacity) {
        this.inputs.capacity.type = 'is-danger'
        this.inputs.capacity.customClass = 'has-text-danger'
        return
      }
      if (isNaN(this.boardingRoom.capacity)) {
        this.inputs.capacity.type = 'is-danger'
        this.inputs.capacity.customClass = 'has-text-danger'
        this.inputs.capacity.message = 'This value must be number'
        return
      }

      this.$axios.$put(
        `http://localhost:8080/api/v1/boarding_rooms/${this.$route.params.id}`,
        {},
        {
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `${this.$store.getters.getToken}`
          },
          params: {
            'name': `${this.boardingRoom.name}`,
            'capacity': `${this.boardingRoom.capacity}`
          }
        }).then(() => {
        this.$buefy.toast.open({
          message: 'Boarding room updated!',
          type: 'is-success'
        })
        this.inputs.name.type = ''
        this.inputs.name.customClass = ''
        this.inputs.name.message = ''
        this.inputs.capacity.type = ''
        this.inputs.capacity.customClass = ''
        this.inputs.capacity.message = ''
      }).catch(() => {
          this.inputs.name.type = 'is-danger'
          this.inputs.name.customClass = 'has-text-danger'
          this.inputs.name.message = 'Name already in use'
          this.$buefy.notification.open({
            duration: 3000,
            message: `Boarding room name: <b>${this.boardingRoom.name}</b> is already in use`,
            type: 'is-warning',
            hasIcon: true
          })
        })

      console.log('update ' + this.boardingRoom.name)
    },
    assignTerminal(){
      if (this.terminalToAdd !== ''){
        let terminal
        if(this.terminalToAdd === 'National')
          terminal = this.allTerminals.find(terminal => terminal.type === 1)
        if(this.terminalToAdd === 'International')
          terminal = this.allTerminals.find(terminal => terminal.type === 2)

        this.$axios.$post(
          `http://localhost:8080/api/v1/terminals/${terminal.id}`,
          {},
          {
            headers: {
              'Authorization': `${this.$store.getters.getToken}`
            },
            params: {
              'boardingRoomId': `${this.$route.params.id}`
            }
          }
        ).then(() => {
          this.boardingRoom.terminal = terminal
          this.$parent.setTerminal([terminal])
          this.disableTerminalAssign()
        })
      }
      else{
        this.$buefy.notification.open({
          duration: 2000,
          message: `Select an <b>available terminal</b> first`,
          type: 'is-danger',
          hasIcon: true
        })
      }
    }
  }
}
</script>
