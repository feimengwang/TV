# TV

I met the error:
C:\Users\IBM_ADMIN\Downloads\TV\app\src\main\AndroidManifest.xml:19:13-102 Error:
Attribute activity#io.vov.vitamio.activity.InitActivity@configChanges value=(orientation|screenSize|smallestScreenSize|keyboard|keyboardHidden) from AndroidManifest.xml:19:13-102
<is also present at [TV:vitamio:unspecified] AndroidManifest.xml:18:13-113 value=(orientation|screenSize|smallestScreenSize|keyboard|keyboardHidden|navigation).
<Suggestion: add 'tools:replace="android:configChanges"' to <activity> element at AndroidManifest.xml:18:9-22:62 to override.

Resolve this by add 'tools:replace="android:configChanges"'
