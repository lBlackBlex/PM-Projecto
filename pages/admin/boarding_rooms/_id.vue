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
          <b-tag type="is-info" size="is-large">{{ boardingRoom.id }}</b-tag>
        </b-taglist>
        <BoardingRoomForm
          :boardingRoom="boardingRoom"
          :showButton="false"
          ref="boarding_room_form"/>
        <br>
        <b-message type="is-info" has-icon>
          Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce id fermentum quam. Proin sagittis, nibh id
          hendrerit imperdiet, elit sapien laoreet elit
        </b-message>

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
              :data="terminal"
              :checked-rows.sync="checkedRows"
              checkbox-position="left"
              checkable>
              <b-table-column field="id" label="ID" v-slot="props">
                {{ props.row.id }}
              </b-table-column>
              <b-table-column field="boarding_rooms" label="Boarding Rooms" v-slot="props">
                <b-taglist>
                  <b-tag
                    v-if="terminalBoardingRooms(props.row) !== null"
                    v-for="item in terminalBoardingRooms(props.row)"
                    type="is-primary">
                    {{ item }}
                  </b-tag>
                </b-taglist>
              </b-table-column>
              <b-table-column field="etd" label="Type" v-slot="props">
                <b-tag type="is-info is-light">{{ formatType(props.row.type) }}</b-tag>
              </b-table-column>
            </b-table>
          </b-tab-item>
          <b-tab-item label="Selected">
            <pre>{{ checkedRows }}</pre>
          </b-tab-item>
        </b-tabs>
      </section>
    </div>
  </section>
</template>

<script>
import BoardingRoomForm from "~/components/BoardingRoomForm";

export default {
  middleware: ['check-auth', 'auth'],
  components: [
    BoardingRoomForm,
  ],
  created() {
    this.getBoardingRoom()
    this.getTerminals()
  },
  data() {
    return {
      boardingRoom: null,
      terminal: [],
      checkedRows: []
    }
  },
  methods: {
    goBack() {
      this.$router.back()
    },
    getBoardingRoom() {
      const boardingRooms = this.$store.getters.boardingRooms
      this.boardingRoom = boardingRooms.find(boarding_room => boarding_room.id === this.$route.params.id)
    },
    getTerminals() {
      if (this.boardingRoom.terminal !== null)
        this.terminal = [this.boardingRoom.terminal]
    },
    getName(name) {
      if (this.checkedRows.length > 1)
        name += 's'
      return name
    },
    formatType(type) {
      if (type === 1)
        return 'National'
      else
        return 'International'
    },
    terminalBoardingRooms(terminal) {
      return terminal.boardingRooms.map(boarding_room => `${boarding_room.name}`)
    },
    confirmDelete() {
      const title = 'Deleting ' + this.getName('Terminal')
      const confirmText = 'Delete ' + this.getName('Terminal')
      this.$buefy.dialog.confirm({
        title: title,
        message: `Are you sure you want to <b>delete</b> this ${this.getName('Route')}? This action cannot be undone.`,
        confirmText: confirmText,
        type: 'is-danger',
        hasIcon: true,
        onConfirm: () => this.removeBoardingRoomTerminals()
      })
    },
    removeBoardingRoomTerminals() {
      this.$axios.$delete(
        `http://localhost:8080/api/v1/terminals/${this.terminal[0].id}/${this.$route.params.id}`,
        {
          headers: {
            'Authorization': `${this.$store.getters.getToken}`
          },
        }).then(() => {
        this.terminal = []
        this.checkedRows = []
        this.$refs.boarding_room_form.enableTerminalAssign()
        this.$buefy.toast.open({
          message: this.getName('Terminal') + ' deleted!',
          type: 'is-danger'
        })
      })
    },
    setTerminal(terminal){
      if(!terminal[0].boardingRooms.find(boarding_room => boarding_room.id === this.boardingRoom.id)){
        const boardingRoomToAdd = this.boardingRoom
        delete boardingRoomToAdd.terminal
        terminal[0].boardingRooms.push(boardingRoomToAdd)
      }else{
        if(terminal[0].boardingRooms.find(boarding_room => boarding_room.id === this.boardingRoom.id).hasOwnProperty('terminal'))
          delete terminal[0].boardingRooms.find(boarding_room => boarding_room.id === this.boardingRoom.id).terminal
      }
      this.terminal = terminal
    }
  }
}
</script>
