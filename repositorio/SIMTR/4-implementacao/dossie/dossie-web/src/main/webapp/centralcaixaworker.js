'use strict';

var applicationServerPublicKey = 'BB173l8rJtk3Q2PUyoCNHszuZ7EHUZdSRFQFFUwuHQB9X0AmssVKloJZuu6+9L9MnCI6piKFCZuIsyeGz9nWfcc=';

function urlB64ToUint8Array(base64String) {
  var padding = '='.repeat((4 - base64String.length % 4) % 4);
  var base64 = (base64String + padding)
    .replace(/\-/g, '+')
    .replace(/_/g, '/');

  var rawData = window.atob(base64);
  var outputArray = new Uint8Array(rawData.length);

  for (var i = 0; i < rawData.length; ++i) {
    outputArray[i] = rawData.charCodeAt(i);
  }
  return outputArray;
}

var json = null;

self.addEventListener('push', function(event) {
  console.log('[Service Worker] Push Received.');
  console.log('[Service Worker] Push had this data: "${event.data.text()}"');
  
  var title = "Caixa Web-PUSH";
  var body = null;  
  var icon = 'images/icon.png';
  var badge = 'images/badge.png';
  try {
  	json = JSON.parse(event.data.text());
  	
  	if (json.message) {
  		body = json.message;
  	}  	
  	if (json.title) {
  		title = json.title;
  	}  	
  	if (json.icon) {
  		icon = json.icon;
  	}
  	if (json.badge) {
  		badge = json.badge;
  	}
    if (json.image) {
      icon = json.image;
      badge = json.image;
    }
  	
	} catch (ignore) {
		body = event.data.text();
	}

  var options = {
    body: body,
    icon: icon,
    badge: badge
  };

  event.waitUntil(self.registration.showNotification(title, options));
});

self.addEventListener('notificationclick', function(event) {
  console.log('[Service Worker] Notification click Received.');
  var mURL = 'https://www.caixa.gov.br/';
  try {
  	if (json && json.url) {
  		mURL = json.url;
  	}
  } catch (ignore) {
  }
  
  console.log("mURL: " + mURL);

  event.notification.close();

  event.waitUntil(
    clients.openWindow(mURL)
  );
});

self.addEventListener('pushsubscriptionchange', function(event) {
  console.log('[Service Worker]: \'pushsubscriptionchange\' event fired.');
  var applicationServerKey = urlB64ToUint8Array(applicationServerPublicKey);
  event.waitUntil(
    self.registration.pushManager.subscribe({
      userVisibleOnly: true,
      applicationServerKey: applicationServerKey
    })
    .then(function(newSubscription) {
      // TODO: Send to application server
      console.log('[Service Worker] New subscription: ', newSubscription);
    })
  );
});
