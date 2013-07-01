phoneOnServo
============
<p dir="RTL">
لرؤية المشروع: اضغط هنا http://www.youtube.com/watch?v=a-1L8vMyuBA
</p>

<h2 dir="RTL">تعليمات الإستخدام</h3>
<h3 dir="RTL">أندرويد:</h3>
<p dir="RTL"> في﻿ ملف Main.java الخاص بالأندرويد قم بتعديل الآتي:</p>
 
<p dir="RTL">
اذهب إلى سطر 36 أو ابحث عن "UUID MY_UUID" وقم بوضع رقم UUID الخاص بجوالك وللحصول عليه قم بتنفيذ الأوامر التالية:
</p>
<p>
TelephonyManager tManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
<br>
String uuid = tManager.getDeviceId();
<br>
Log.d("UUID", "My UUID is: " + uuid);
</p>
<p dir="RTL">
وبعدها اذهب إلى سطر 40 أو ابحث عن "String address" وقم بكتابة MAC Address الخاص بقطعة البلوتوث.
</p>
<h3 dir="RTL">أردوينو:</h3>
<p dir="RTL">
<p dir="RTL">انسخ ملف IRemote والموجود في مجلد Microcontroller/libraries والصقه في مجلد مكتبات الأردوينو والموجود في الغالب (أو في مجلد أخر إذا قمت بتغيره عند تنصيب الأردوينو) في: "C:\Program Files (x86)\Arduino\libraries"
</p>
<p dir="RTL">ملاحظة: إذا كنت تمتلك ريموت تلفاز من شركة أخرى غير شركة سوني تحتاج إلى فعل الآتي:<p>
<p dir="RTL">قم بتشغيل هذا المثال IRrecvDemo.ino والموجود في هذا الرابط IRremote / examples / IRrecvDemo / IRrecvDemo.ino وتأكد من أن مستقبل أشعة الإنفراريد موصول بـ Pin 11. عند ضغط أي زر في ريموت التلفاز سيظهر الكود الخاص بالزر في شاشة Serial Monitor الخاصة بالأردوينو. سجل الأكواد التي تهمك واكتب قيمها في ملف phone_servo.ino الموجود في مجلد  Microcontroller / phone_servo / phone_servo.ino في هذا السطر
<br>
enum {
    RIGHT = 3280, LEFT = 720, UP = 752, DOWN = 2800, PICTURE = 3928, VIDEO = 1488, FACE = 14318, CAMERAON = 2704, TOOLS = 14057, NOTHING = 0, DONOTHING = -1,
<br>
لاحظ أن كل كود أعلاه يمثل قيمة زر في ريموت شركة سوني. 


<h2 dir="RTL">القطع المستخدمة:</h2>
<p dir="RTL">
- أردوينو (أنا أستخدمت نفس المايكروكنترولر الموجود في الأردوينو)
<br>
http://www.amazon.com/Arduino-UNO-board-DIP-ATmega328P/dp/B006H06TVG/ref=sr_1_1?ie=UTF8&qid=1371296604&sr=8-1&keywords=Arduino+UNO

</p>
<br>
<p dir="RTL">
- جهاز أندرويد 
</p>
<br>
<p dir="RTL">
- ريموت للتفاز
</p>
<br>
<p dir="RTL">
- دايود أشعة تحت حمراء (Infrared LED):
<br>
https://www.sparkfun.com/products/9349
<br>
http://www.radioshack.com/product/index.jsp?productId=2062565
<br>
RadioShack 
ملاحظة: لم أجرب هذه المستقبلات حيث أن المستقبل الذي أملكه اشترتيه من 

 ولم أجد نفس الموديل في الإنترنت. لكن المفروض أن جميع المستقبلات تعمل بنفس الطريقة
</p>
<br>
<p dir="RTL">
- قطعة بلوتوث:
<br>
https://www.sparkfun.com/products/10268
<br>
ملاحظة: هذه القطعة غالية نوعًا ما. من الممكن إستخدام أي قطعة بلوتوث أخرى
</p>
<br>
<p dir="RTL">
- محرك Servo:
<br>
https://www.adafruit.com/products/154
</p>
<br>
<p dir="RTL">
- دايود LED متعدد الألوان (7 ألوان):
<br>
https://www.sparkfun.com/products/105
</p>
<br>
<p dir="RTL">
- قاعدة جوال:
<br>
http://www.amazon.com/gp/product/B008VI7ORA/ref=oh_details_o02_s00_i00?ie=UTF8&psc=1
</p>
