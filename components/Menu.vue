<template>
  <b-menu>
  <b-menu-list label="Menu">
    <b-menu-item icon="home" label="Home" tag="router-link" :to="{ path: '/' }"></b-menu-item>
    <b-menu-item icon="information-outline" :active="isActive">
      <template #label="props">
        Travel Information
        <b-icon class="is-pulled-right" :icon="!props.expanded ? 'menu-down' : 'menu-up'"></b-icon>
      </template>
      <b-menu-item label="Flihgt Status" icon="information-outline" tag="router-link" :to="{path: '/routes'}"></b-menu-item>
    </b-menu-item>
    <b-menu-item icon="account" :active="isActive">
      <template #label="props">
        My Account
        <b-icon class="is-pulled-right" :icon="!props.expanded ? 'menu-down' : 'menu-up'"></b-icon>
      </template>
      <b-menu-item label="Account data"></b-menu-item>
      <b-menu-item label="Your Trips"></b-menu-item>
    </b-menu-item>
  </b-menu-list>
  <b-menu-list>
  </b-menu-list>
    <b-menu-list label="Administrator Actions" v-if="getUserRank === 'ADMIN'">
      <b-menu-item icon="cog" :active="isActive">
        <template #label="props">
          Administrator
          <b-icon class="is-pulled-right" :icon="!props.expanded ? 'menu-down' : 'menu-up'"></b-icon>
        </template>
        <b-menu-item icon="view-dashboard-outline" label="Dashboard" tag="router-link" :to="{ path: '/admin' }"></b-menu-item>
        <b-menu-item icon="format-list-bulleted" label="Flights" tag="router-link" :to="{ path: '/admin/flights' }"></b-menu-item>
        <b-menu-item icon="airplane" label="Planes" tag="router-link" :to="{ path: '/admin/planes' }"></b-menu-item>
        <b-menu-item icon="database-cog-outline" label="Airlines" tag="router-link" :to="{ path: '/admin/airlines' }"></b-menu-item>
        <b-menu-item icon="database-cog-outline" label="Terminals" tag="router-link" :to="{ path: '/admin/terminals' }"></b-menu-item>
        <b-menu-item icon="database-cog-outline" label="Boarding Rooms" tag="router-link" :to="{ path: '/admin/boarding_rooms' }"></b-menu-item>
        <b-menu-item icon="account-group" label="Users" tag="router-link" :to="{ path: '/admin/users' }"></b-menu-item>
      </b-menu-item>
    </b-menu-list>
  <b-menu-list label="Actions">
    <b-menu-item icon="account-plus" label="Register" v-if="!this.$store.getters.isUserLoggedIn" tag="router-link" :to="{ path: '/register' }"></b-menu-item>
    <b-menu-item icon="login" label="Login" v-if="!this.$store.getters.isUserLoggedIn" tag="router-link" :to="{ path: '/login' }"></b-menu-item>
    <b-menu-item icon="logout" @click="logout" label="Logout" v-if="this.$store.getters.isUserLoggedIn"></b-menu-item>
  </b-menu-list>
</b-menu>
</template>

<script>
export default {
  computed: {
    getUserRank: function() {
      return this.$store.getters.getUserRank
    }
  },
  methods: {
    logout(){
        this.$store.dispatch('logoutUser')
        this.$router.push('/')
    },
  }
}
</script>
