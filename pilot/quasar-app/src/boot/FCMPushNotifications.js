import { Notify } from 'quasar'
import { EventBus } from '../utils/eventBus'

export default ({ app, router, store, vue }) => {
  function getToken()
  {
    cordova.plugins.firebase.messaging.getToken().then(function(token) {
      console.log("Got device token: "+ token);
      store.dispatch("fcmToken/setToken", token);
    });
  }

  function setNotificationEvents()
  {
    if (cordova.platformId!="browser")
    {
      cordova.plugins.firebase.messaging.requestPermission().then(function(token) {
        console.log("APNS device token: "+ token);
      });

      // The event handler function.
      const contentReadyHandler = function() {
        getToken()

        cordova.plugins.firebase.messaging.onTokenRefresh(function() {
          console.log("Device token updated");
          getToken()
        });

        //ForeGround
        cordova.plugins.firebase.messaging.onMessage(function(payload) {
          console.log("New foreground FCM message: "+ JSON.stringify(payload));
          try {
            console.log("vue notify");
            Notify.create({
              message: payload.gcm.body,
              caption: payload.gcm.title,
              multiLine: true,
              color: 'secondary'
            })
          }catch (e) {
            console.log("error" + e)
          }

        });
        //Backgorund
        cordova.plugins.firebase.messaging.onBackgroundMessage(function(payload) {
          console.log("New background FCM message: "+ JSON.stringify(payload));
        });
      }


      EventBus.$on('contentFrameReady', contentReadyHandler);

    }
  }

  document.addEventListener("deviceready", function() {
    console.log(" deviceready");
    setNotificationEvents();

  }, false);
}
