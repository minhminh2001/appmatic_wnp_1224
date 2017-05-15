# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\dev\android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-dontwarn
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*


-keep public class * extends android.support.v4.**
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity      # 保持哪些类不被混淆
-keep public class * extends android.app.Application   # 保持哪些类不被混淆
-keep public class * extends android.app.Service       # 保持哪些类不被混淆
-keep public class * extends android.content.BroadcastReceiver  # 保持哪些类不被混淆
-keep public class * extends android.content.ContentProvider    # 保持哪些类不被混淆
-keep public class * extends android.app.backup.BackupAgentHelper # 保持哪些类不被混淆
-keep public class * extends android.preference.Preference        # 保持哪些类不被混淆
-keep public class com.android.vending.licensing.ILicensingService    # 保持哪些类不被混淆

#保护注解
-keepattributes *Annotation*



-keep class com.whitelabel.app.model.** { *; }
-dontwarn com.whitelabel.app.model.**

-keep class javax.annotation.**{*;}
-dontwarn javax.annotation.**



-keep class com.lidroid.xutils.** { *; }
-keep public class * extends com.lidroid.xutils.**
-keepattributes Signature
-keepattributes *Annotation*
-keep public interface com.lidroid.xutils.** {*;}
-dontwarn com.lidroid.xutils.**
-keepclasseswithmembers class com.jph.android.entity.** {
    <fields>;
    <methods>;
}
-keep class com.google.gson.** {*;}
#-keep class com.google.**{*;}
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }
-keep class com.google.** {
    <fields>;
    <methods>;
}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-dontwarn com.google.gson.**



-keep class **.R$* {   *;  }

-keep class com.facebook.** { *; }
-dontwarn com.facebook.**
-keep class com.androidquery.** { *; }
-dontwarn com.androidquery.**


-keep class org.acra.** { *; }
-dontwarn org.acra.**

-keep class org.apache.** { *; }

-dontwarn org.apache.**

-keep class com.mobileapptracker.** { *; }

-dontwarn com.mobileapptracker.**

-keep class com.nostra13.** { *; }

-dontwarn com.nostra13.**

-keep class com.google.android.gms.**{*;}
-dontwarn com.google.android.gms.**
-keep class com.devicecollector.**{*;}
-dontwarn com.devicecollector.**

-keep class com.molpay.molpaysdk.** { *; }

-dontwarn com.molpay.molpaysdk.**





-keep class net.simonvt.** { *; }
-dontwarn net.simonvt.**

-keep class android.support.** { *; }
-dontwarn android.support.**



-keep class com.nnacres.app.model.** { *; }

-dontwarn com.nnacres.app.model.**

-keep class io.card.payment.** { *; }

-dontwarn io.card.payment.**

-keep class android.support.v4.** { *; }
-dontwarn android.support.v4.**

-keep class android.support.v7.** { *; }
-dontwarn android.support.v7.**


-keep class sun.misc.**{*;}
-dontwarn sun.misc.**

-keep class android.support.annotation.** { *; }

-dontwarn android.support.annotation.**


-keep class com.android.volley.**{*;}
-dontwarn com.android.volley.**


-dontwarn okhttp3.**

-dontwarn okio.**

-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.* { *;}
-dontwarn okio.**

-dontwarn com.bumptech.glide.**
-keep class com.bumptech.glide.** { *; }

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule

-keep class com.bumptech.glide.integration.okhttp3.OkHttpGlideModule


-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}



-keepclasseswithmembernames class * {  # 保持 native 方法不被混淆
    native <methods>;
}
-keepclasseswithmembers class * {   # 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {# 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * extends android.app.Activity { # 保持自定义控件类不被混淆
    public void *(android.view.View);
}
-keepclassmembers enum * {     # 保持枚举 enum 类不被混淆
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable { # 保持 Parcelable 不被混淆
    public static final android.os.Parcelable$Creator *;
}


-keep class butterknife.compiler.**{*;}
-dontwarn butterknife.compiler.**

-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}
        -keep class com.squareup.**{*;}
        -dontwarn com.squareup.**
        -keep class com.google.auto.**{*;}
        -dontwarn  com.google.auto.**


    # Retrofit
     -dontwarn retrofit2.**
    -keep class retrofit2.** { *; }

    # RxJava RxAndroid
    -dontwarn sun.misc.**
    -keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
        long producerIndex;
        long consumerIndex;
    }

    -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
        rx.internal.util.atomic.LinkedQueueNode producerNode;
    }
    -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
        rx.internal.util.atomic.LinkedQueueNode consumerNode;
    }


