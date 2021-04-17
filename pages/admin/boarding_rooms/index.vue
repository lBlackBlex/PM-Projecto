<template>
  <section class="section">
    <div class="columns is-mobile">
      <div class="column">
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
              :data="boardingRooms"
              :checked-rows.sync="checkedRows"
              checkbox-position="left"
              checkable>
              <b-table-column field="id" label="Flight Number" v-slot="props">
                {{ props.row.id }}
              </b-table-column>
              <b-table-column field="terminal" label="Terminal" v-slot="props">
                <b-taglist>
                  <b-tag v-if="boardingRoomTerminal(props.row.terminal) === 'National'" type="is-info">
                    {{ boardingRoomTerminal(props.row.terminal) }}
                  </b-tag>
                  <b-tag v-if="boardingRoomTerminal(props.row.terminal) === 'International'" type="is-success">
                    {{ boardingRoomTerminal(props.row.terminal) }}
                  </b-tag>
                  <b-tag v-if="boardingRoomTerminal(props.row.terminal) === 'No Terminal'" type="is-light">
                    {{ boardingRoomTerminal(props.row.terminal) }}
                  </b-tag>
                </b-taglist>
              </b-table-column>
              <b-table-column field="capacity" label="Capacity" v-slot="props">
                {{ props.row.capacity }}
              </b-table-column>
              <b-table-column field="name" label="Name" v-slot="props">
                {{ props.row.name }}
              </b-table-column>
            </b-table>
          </b-tab-item>
          <b-tab-item :label="checkedRows.length <= 1 ? 'Selected' : 'Selecteds'">
            <pre>{{ checkedRows }}</pre>
          </b-tab-item>
        </b-tabs>
      </div>
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
      boardingRooms: this.$store.getters.boardingRooms,
      checkedRows: [],
    }
  },
  methods: {
    edit() {
      this.$router.push('boarding_rooms/' + this.checkedRows[0].id)
    },
    create() {
      this.$router.push('boarding_rooms/create')
    },
    boardingRoomTerminal(terminal) {
      if (terminal === null) return 'No Terminal'
      if (terminal.type === 1) return 'National'
      if (terminal.type === 2) return 'International'
    },
    getEntityName() {
      let entity = 'Boarding Room'
      if (this.checkedRows.length > 1)
        entity += 's'
      return entity
    },
    confirmDelete() {
      const title = 'Deleting ' + this.getEntityName()
      const confirmText = 'Delete ' + this.getEntityName()
      this.$buefy.dialog.confirm({
        title: title,
        message: `Are you sure you want to <b>delete</b> this ${this.getEntityName().toLocaleLowerCase()}? This action cannot be undone.`,
        confirmText: confirmText,
        type: 'is-danger',
        hasIcon: true,
        onConfirm: () => this.removeBoardingRooms()
      })
    },
    removeBoardingRooms() {
      const removedBoardingRooms = []
      this.checkedRows.map(boarding_room => {
        this.$axios.$delete(
          `http://localhost:8080/api/v1/boarding_rooms/${boarding_room.id}`,
          {
            headers: {
              'Authorization': `${this.$store.getters.getToken}`
            }
          })
        removedBoardingRooms.push(boarding_room.id)
      })
      this.boardingRooms = this.boardingRooms.filter(e => !removedBoardingRooms.includes(e.id))
      this.checkedRows=[]
      this.$buefy.toast.open({
        message: this.getEntityName() + ' deleted!',
        type: 'is-danger'
      })
    }
  }
}
</script>

