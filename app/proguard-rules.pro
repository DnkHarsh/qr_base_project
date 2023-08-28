# Add project specific ProGuard rules here.

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-dontwarn java.lang.invoke.*
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions

-dontwarn com.android.billingclient.**
-keep class com.android.billingclient.** { *; }


## Android and google

-dontwarn com.google.**
-keep class com.google.** { *; }
-keep interface com.google.**{*;}

## Firebase
-keep class com.google.firebase.**{*;}
-keep class com.firebase.** { *; }


# Retrofit 2
# Platform calls Class.forName on types which do not exist on Android to determine platform.
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontnote retrofit2.Platform
-dontwarn retrofit2.Platform

# Okio which retrofit 2 usses as a internal hood.
-dontwarn okio.**
-dontwarn retrofit2.converter.**
-keep class retrofit2.converter.**{*;}

-keep class com.squareup.**{*;}
-dontwarn com.squareup.**

-keep class com.demo.baseproject.notification.**{*;}
-dontwarn com.demo.baseproject.notification.**

-keep class com.demo.baseproject.datalayers.**{*;}
-dontwarn com.demo.baseproject.datalayers.**

## Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
 <init>(...);
}
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keep class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder$InternalRewinder {
  *** rewind();
}


-ignorewarnings

-dontwarn com.module.**
-keep class com.module.** { *; }

-dontwarn com.common.**
-keep class com.common.** { *; }

# inmobi
-keepattributes SourceFile,LineNumberTable
-keep class com.inmobi.** { *; }
-keep public class com.google.android.gms.**
-dontwarn com.google.android.gms.**
-dontwarn com.squareup.picasso.**
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient{
     public *;
}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info{
     public *;
}
# skip the Picasso library classes
-keep class com.squareup.picasso.** {*;}
-dontwarn com.squareup.okhttp.**
# skip Moat classes
-keep class com.moat.** {*;}
-dontwarn com.moat.**
# skip IAB classes
-keep class com.iab.** {*;}
-dontwarn com.iab.**