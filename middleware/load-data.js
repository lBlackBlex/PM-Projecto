export default function (context) {
  console.log('loading admin data')
  context.store.dispatch('loadAdminData')
}
