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
        </div>
        <b-tabs>
          <b-tab-item label="Table">
            <b-table
              :data="terminals"
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
                  <b-tag v-if="terminalBoardingRooms(props.row) === null" type="is-light">No Boarding Rooms</b-tag>
                </b-taglist>
              </b-table-column>
              <b-table-column field="type" label="Type" v-slot="props">
                <b-tag :type="props.row.type === 1 ? 'is-info': 'is-success'">{{ formatType(props.row.type) }}</b-tag>
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

export default {
  middleware: ['check-auth', 'auth', 'load-data'],
  data() {
    return {
      checkedRows: [],
      terminals: this.$store.getters.terminals,
    }
  },
  methods: {
    formatType(type){
      if(type === 1)
        return 'National'
      else
        return 'International'
    },
    terminalBoardingRooms(terminal) {
      let boarding_rooms = null
      if (terminal.boardingRooms.length !== 0)
        boarding_rooms = terminal.boardingRooms.map(boarding_room => `${boarding_room.name}`)
      return boarding_rooms
    },
    edit() {
      this.$router.push(`terminals/${this.checkedRows[0].id}`)
    },
  }
}
</script>
