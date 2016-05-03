Fragment ViewPager
========

[![Release](https://img.shields.io/badge/jCenter-1.0.0-brightgreen.svg)](https://bintray.com/sbrukhanda/maven/FragmentViewPager)
[![GitHub license](https://img.shields.io/badge/license-Apache%20Version%202.0-blue.svg)](https://github.com/sbrukhanda/fragmentviewpager/blob/master/LICENSE.txt)

An extended `ViewPager` which provides callbacks for the visibility state of its `Fragment` pages through the `FragmentVisibilityListener` interface. Also, it is possible to nest a `FragmentViewPager` inside another `FragmentViewPager` and still maintain this functionality.

Download
--------

Maven:
```xml
<dependency> 
  <groupId>com.sbrukhanda.fragmentviewpager</groupId> 
  <artifactId>fragmentviewpager</artifactId> 
  <version>1.0.0</version> 
  <type>pom</type> 
</dependency>
```
or Gradle:
```groovy
compile 'com.sbrukhanda.fragmentviewpager:fragmentviewpager:1.0.0'
```

Usage
--------

1) Attach `FragmentViewPager` programmatically or via XML to an `Activity` or `Fragment`, as you would with native `ViewPager`.

2) Set `FragmentViewPager`'s adapter. It should be an instance of `com.sbrukhanda.fragmentviewpager.adapters.FragmentPagerAdapter` or `com.sbrukhanda.fragmentviewpager.adapters.FragmentStatePagerAdapter`, or else callbacks for the visibility state of Fragment pages wont work correctly. This classes are carbon copies of the repsective classes from the support library, with just a few tweaks that allow `FragmentViewPager` to do its magic ;-)

3) Override `onResumeFragments()` of the hosting `Activity` and call `notifyPagerVisible()`:
```java
private FragmentViewPager mFragmentsPager;

@Override
public void onResumeFragments() {
    super.onResumeFragments();
    mFragmentsPager.notifyPagerVisible();
}
```
or `onResume()` of the hosting `Fragment` and call `notifyPagerVisible()`:
```java
private FragmentViewPager mFragmentsPager;

@Override
public void onResume() {
    super.onResume();
    mFragmentsPager.notifyPagerVisible();
}
```

4) Override `onPause()` of the hosting `Activity` or `Fragment` and call `notifyPagerInvisible()`: 
```java
private FragmentViewPager mFragmentsPager;

@Override
public void onPause() {
    super.onPause();
    mFragmentsPager.notifyPagerInvisible();
}
```

5) Implement `FragmentVisibilityListener` on all `Fragment` pages that you wish to receive callbacks for their visibility state.

You are ready to go :-)

Nested FragmentViewPager
--------

Although, it's not a good idea to have multiple levels of `ViewPager` inside your views, sometimes theres no other way around it. `FragmentViewPager` supports an infinite amount of levels with this slight adjustment:

--------

If for some reason, you are facing difficulties in using `FragmentViewPager` inside your project, I would advice you to check the demo subproject for a working sample.

License
=======

```
  Copyright 2016 Serhiy Brukhanda

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0
  
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
```
