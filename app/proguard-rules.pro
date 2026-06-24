# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in the SDK tools.

# Keep Parse SDK classes
-keep class com.parse.** { *; }
-dontwarn com.parse.**
