Fragment ViewPager
========

[![Release](https://img.shields.io/badge/jCenter-1.0.0-brightgreen.svg)](https://bintray.com/sbrukhanda/maven/FragmentViewPager)
[![GitHub license](https://img.shields.io/badge/license-Apache%20V2%20license-blue.svg)](https://github.com/sbrukhanda/fragmentviewpager/blob/master/LICENSE.txt)

An extended ViewPager which provides callbacks for the visibility state of its Fragment pages through the **FragmentVisibilityListener** interface. Also, it is possible to nest a **FragmentViewPager** inside another **FragmentViewPager** and still maintain this functionality.

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

<ol>
  <li>
    Attach FragmentViewPager programmatically or via XML to an Activity or Fragment, as you would with native ViewPager.
  </li>
  <li>
    Set FragmentViewPager's adapter.
    
    **_Note:_** The provided PagerAdapter should be an instance of com.sbrukhanda.fragmentviewpager.adapters.FragmentPagerAdapter or com.sbrukhanda.fragmentviewpager.adapters.FragmentStatePagerAdapter, or else callbacks for the visibility state of Fragment pages wont work.
  </li>
</ol>

3 Override **onResumeFragments()** method of the hosting Activity and call **notifyPagerVisible()** inside it. 

Example:

```
private FragmentViewPager mFragmentsPager;

@Override
public void onResumeFragments() {
    super.onResumeFragments();
    mFragmentsPager.notifyPagerVisible();
}
```

4. Override **onPause()** method of the hosting Activity and call **notifyPagerInvisible()** inside it. 

Example:

```
private FragmentViewPager mFragmentsPager;

@Override
public void onPause() {
    super.onPause();
    mFragmentsPager.notifyPagerInvisible();
}
```

5. Implement **FragmentVisibilityListener** on all Fragment pages that you wish to receive callbacks for their visibility state.

Done! :-)

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
