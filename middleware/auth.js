export default function (context){
  if(!context.store.getters.isUserLoggedIn){
      context.redirect('/login')
  }
}
