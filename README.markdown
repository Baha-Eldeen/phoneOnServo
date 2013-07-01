phoneOnServo
============
<p dir="RTL">
لرؤية المشروع اضغط هنا http://www.youtube.com/watch?v=a-1L8vMyuBA
</p>

============

<p dir="RTL"><strong>تعليمات الإستخدام</strong>:</p>

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

================
<p dir="RTL"><strong>القطع المستخدمة</strong>:</p>
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
