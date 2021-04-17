<template>
  <section>
    <div class="buttons">
      <b-button
        v-if="specialType!==true"
        label="Edit selected"
        type="is-primary"
        icon-left="pencil"
        :disabled="checkedRows.length===0 || checkedRows.length>1"
        @click="edit"/>
        <b-button
          v-if="specialType!== true"
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
          :data="data"
          :columns="columns"
          :checked-rows.sync="checkedRows"
          checkbox-position="left"
          checkable>
        </b-table>
      </b-tab-item>

      <b-tab-item :label="checkedRows.length <= 1 ? 'Selected' : 'Selecteds'">
        <pre>{{ checkedRows }}</pre>
      </b-tab-item>
    </b-tabs>
  </section>

</template>

<script>
export default {
  props: {
    data: {
      type: Array,
      required: true
    },
    columns: {
      type: Array,
      required: true
    },
    control: {
      type: String,
      required: true
    },
    special: {
      type: Boolean,
      required: false
    }
  },
  data() {
    return {
      currentPage: 1,
      checkedRows: [],
      type: this.control,
      specialType: this.special,
    }
  },
  methods: {
    getEntityName(){
      let entity
      if (this.specialType === true)
        entity = `${this.type.split(',')[1]}`
       else
        entity = `${this.type}`

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
        onConfirm: () => this.remove()
      })
    },
    edit() {
      if (this.type === 'Airline')
        this.$router.push('airlines/' + this.checkedRows[0].id)
      if(this.type === 'Terminal')
        this.$router.push('terminals/' + this.checkedRows[0].id)
      // if(this.type === 'Plane')
      //   this.$router.push('planes/' + this.checkedRows[0].id)

      console.log('edit ' + this.type + ' ' + this.checkedRows[0].id)
    },
    remove() {
      const entityName = this.getEntityName().replace(/\s+/g, '')
      if (this.specialType) {
        // Flights/_id
        if(this.getEntityName() === 'Airline' || this.getEntityName() === 'Airlines'){
          this.$parent.removeRouteAirlines(this.checkedRows, this.getEntityName())
          this.checkedRows = []
        }
        // Terminals/_id
        if (entityName === 'BoardingRoom' || entityName === 'BoardingRooms') {
          this.$parent.removeBoardingRoom(this.checkedRows, this.getEntityName())
          this.checkedRows = []
        }
        console.log('delete special ' + entityName)

      } else {
        if (this.type === 'Airline') {
          this.$parent.removeAirlines(this.checkedRows, this.getEntityName())
          this.checkedRows = []
        }
        // //Boarding_Rooms/index
        // if(this.type.replace(/\s+/g, '') === 'BoardingRoom'){
        //   this.$parent.removeBoardingRooms(this.checkedRows, this.getEntityName())
        //   this.checkedRows = []
        // }
      }
    },
    create(){
      if(this.type === 'Airline')
        this.$router.push('airlines/create')
      // if(this.type === 'Plane')
      //   this.$router.push('planes/create')
      console.log('Create ' + this.type)
    }
  }
}
</script>
