<template>
  <section class="section">
    <div class="columns is-mobile">
      <div class="block">
        <b-field label="Name" label-position="on-border">
          <b-input v-model="form.name"/>
        </b-field>
        <b-field label="Last name" label-position="on-border">
          <b-input v-model="form.last_name"/>
        </b-field>
        <b-field label="Email" label-position="on-border">
          <b-input type="email" v-model="form.email" maxlength="30"/>
        </b-field>
        <b-field label="Password" label-position="on-border">
          <b-input type="password" password-reveal v-model="form.password"  maxlength="30"/>
        </b-field>
        <b-button @click="signIn" label="Sign in" type="is-primary"></b-button>
      </div>
    </div>
  </section>
</template>

<script>
import axios from 'axios'

export default {
  data() {
    return {
      form: {
        name: '',
        last_name: '',
        email: '',
        password: '',
      }
    }
  },
  methods: {
    signIn(){
      const headers = {
        'Content-Type': 'application/json',
      }
      const user = JSON.stringify({
        email: this.form.email,
        password: this.form.password,
        name: this.form.name,
        last_name: this.form.last_name
      })
      console.log(user)
      axios({
        method: 'post',
        url: 'http://localhost:8080/api/v1/users',
        data: user,
        headers: headers
      }).then(response => {
        if (response.data != null) {
          this.$router.push('/login')
          this.$buefy.toast.open({
            message: 'Great! now you can login in to the app',
            type: 'is-success'
          })
        }
      })
    }
  }
}
</script>
