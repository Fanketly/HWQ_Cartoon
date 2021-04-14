# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#-keep class com.example.repository.model.SpeciesInfo2Item{
#  private java.lang.String author;
#  private java.lang.String comic_cover;
#  private java.lang.String comic_url;
#  private java.lang.String id;
#  private java.lang.String last_chapter;
#  private java.lang.String last_chapter_url;
#  private java.lang.String last_update_date;
#  private java.lang.String name;
#  private java.lang.String short_name;
#  private java.lang.String status;
#  private java.lang.String type;
#}
#-keep class com.example.util.NetworkUtils{
#public static java.util.concurrent.Flow getData(java.lang.String url);
#}

