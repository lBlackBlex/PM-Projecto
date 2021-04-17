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
          <b-tag type="is-info" size="is-large">{{ terminal.id }}</b-tag>
        </b-taglist>
        <TerminalForm
          :terminal="terminal"/>
        <br>
        <b-message type="is-info" has-icon>
          Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce id fermentum quam. Proin sagittis, nibh id
          hendrerit imperdiet, elit sapien laoreet elit
        </b-message>
        <Table
          class="column"
          :data="boardingRooms"
          :columns="columns"
          :special="true"
          control="Terminal, Boarding Room"/>
      </section>
    </div>
  </section>
</template>

<script>
import Table from "~/components/Table";
import TerminalForm from "~/components/TerminalForm";

export default {
  middleware: ['check-auth', 'auth'],
  components: [
    TerminalForm,
    Table
  ],
  created() {
    this.getTerminal()
    this.getBoardingRooms()
    // this.getRoutes()
  },
  data() {
    return {
      terminal: null,
      boardingRooms: [],
      routes: [],
      columns: [
        {
          field: 'id',
          label: 'ID',
        },
        {
          field: 'capacity',
          label: 'Capacity'
        },
        {
          field: 'name',
          label: 'Name',
        },
      ]
    }
  },
  methods: {
    goBack() {
      this.$router.back()
    },
    getTerminal() {
      const terminals = this.$store.getters.terminals
      this.terminal = terminals.find(terminal => terminal.id === this.$route.params.id)
    },
    getBoardingRooms() {
      this.boardingRooms = this.terminal.boardingRooms
    },
    getRoutes() {
      this.terminal = this.terminal.routes
    },
    removeBoardingRoom(checkedRows, entity) {
      const removedBoardingRooms = []
      checkedRows.map(boarding_room => {
        this.$axios.$delete(
          `http://localhost:8080/api/v1/terminals/${this.$route.params.id}/${boarding_room.id}`,
          {
            headers: {
              'Authorization': `${this.$store.getters.getToken}`
            },
          })
        removedBoardingRooms.push(boarding_room.id)
      })
      console.log(removedBoardingRooms)
      this.boardingRooms = this.boardingRooms.filter(e => !removedBoardingRooms.includes(e.id))
      this.$buefy.toast.open({
        message: entity + ' deleted!',
        type: 'is-danger'
      })
    }
  }
}
</script>
