# k3pler proxy <a href="https://github.com/KeyLo99/k3pler/releases"><img src="https://img.shields.io/github/release/KeyLo99/k3pler.svg"/>
</a>

<a href="https://github.com/KeyLo99/k3pler/issues"><img src="https://img.shields.io/github/issues/KeyLo99/k3pler.svg"/></a>
<a href="https://github.com/KeyLo99/k3pler/pulls"><img src="https://img.shields.io/github/issues-pr/KeyLo99/k3pler.svg"/></a>
<a href="https://github.com/KeyLo99/k3pler/stargazers"><img src="https://img.shields.io/github/stars/KeyLo99/k3pler.svg"/></a>
<a href="https://github.com/KeyLo99/k3pler/network"><img src="https://img.shields.io/github/forks/KeyLo99/k3pler.svg"/></a>
<a href="https://github.com/KeyLo99/k3pler/blob/master/LICENSE"><img src="https://img.shields.io/github/license/KeyLo99/k3pler.svg"/>

### Android network connection blocker and packet analyzer built on top of local HTTP proxy.

[<img src="https://play.google.com/intl/en_us/badges/images/generic/en-play-badge.png"
     alt="Get it on Google Play"
     height="80">](https://play.google.com/store/apps/details?id=com.tht.k3pler)
[<img src="https://f-droid.org/badge/get-it-on.png"
     alt="Get it on F-Droid"
     height="80">](https://f-droid.org/packages/com.tht.k3pler/)

<img src="https://github.com/KeyLo99/k3pler/blob/master/etc/k3plerbg3_splash.png" width="270" height="385"/>

## Features
* Show network traffic in a list (HTTP Request/Response)

<img src="https://github.com/KeyLo99/k3pler/blob/master/etc/page1.png" width="341" height="606"/>

* Gives details about requests/responses with abbreviations in list.
(eg: ```example.com ~ C [H/1.1] _S_ {time}```)
(C -> Connect | H/1.1 -> HTTP version | _S_ -> Success)
* Blacklist connection after getting detailed information
* Edit/Remove item at blacklist - clear blacklist

<img src="https://github.com/KeyLo99/k3pler/blob/master/etc/page2.png" width="341" height="606"/>

* Settings:
    ```
    Proxy Port: Local Proxy port for connection
    Max. Buffer: Maximum response buffer size in bytes
    Match Type:
        Full: Block if request URI equals blacklist item
        Keyword: Block if request URI contains blacklist item
    Blacklist Response: Response status which will be sent to blocked address
    Splash Screen: Show splash screen when app starts
    ```

<img src="https://github.com/KeyLo99/k3pler/blob/master/etc/page3.png" width="341" height="606"/>

* Shows instructions of configuring proxy (WIFI / Mobile Network)

## APK

[Download APK](https://github.com/KeyLo99/k3pler/raw/master/app/dist/k3pler_v1-2.apk)

## Licenses

* [GNU Public License v3](https://www.gnu.org/licenses/gpl.txt)
* [Android v7 RecyclerView Library](https://developer.android.com/topic/libraries/support-library/packages) `(Apache 2.0)`
* [LittleProxy 1.1.2](https://github.com/adamfisk/LittleProxy) `(Apache 2.0)`

## Credit

Copyright (C) 2019 by KeyLo99 https://www.github.com/KeyLo99
