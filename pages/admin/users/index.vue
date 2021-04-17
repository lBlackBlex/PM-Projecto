<template>
  <section class="section">
    <div class="columns is-mobile">
      <Table
        class="column"
        :data="users"
        :columns="columns"
        control="users"/>
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
      users: this.$store.getters.users,
      columns: [
        {
          field: 'id',
          label: 'ID',
        },
        {
          field: 'email',
          label: 'Email',
        },
        {
          field: 'name',
          label: 'Name',
        },
        {
          field: 'last_name',
          label: 'Last Name',
        },
        {
          field: 'role',
          label: 'Role',
        },
      ]
    }
  },
}
</script>

