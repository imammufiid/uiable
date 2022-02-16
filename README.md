
<p align="center">
  <h1 align="center">UIable</h1>
</p>

<p align="center">
  <img src="https://static.republika.co.id/uploads/images/inpicture_slide/098502100-1586781138-5cb5913f6bc07-jisoo-blackpink_.jpg"/>
</p>

<p align="center">
  <a href="LICENSE"><img alt="License" src="https://img.shields.io/badge/License-Apache%202.0-blue.svg"></a>
  <a href="https://github.com/imufiid/uiable/pulls"><img alt="Pull request" src="https://img.shields.io/badge/PRs-welcome-brightgreen.svg?style=flat"></a>
  <a href="https://github.com/imufiid"><img alt="Github" src="https://img.shields.io/github/followers/imufiid?label=follow&style=social"></a>
  <p align="center">Android layout UIable.
</p>

## Features
- [x] UI Draggable
    - stickyX
    - stickyY
    - stickyXY
    - collision (default)
- [x] GboardEditText
- [ ] UI Zoomable (soon)

## Screenshot
---
|![](https://i.ibb.co/hHQ26wB/Collision.gif)|![](https://i.ibb.co/g73KyH8/Sicky-X.gif)|![](https://i.ibb.co/342VPZv/Sticky-Y.gif)|
|--|--|--|
|Draggable Collision|Draggable Sticky-X|Draggable Sticky-Y|

---
|![](https://i.ibb.co/THf59P7/Sticky-XY.gif)|![](https://i.ibb.co/4sN3jcq/ezgif-4-c2339e83fe.gif)|
|--|--|
|Draggable Sticky-XY|GBoardEditText|
---

## Example code
### Draggable
```xml
<com.mufid.uiable.Draggable
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:layout_constraintTop_toTopOf="parent"
    app:layout_constraintStart_toStartOf="parent"
    app:draggable_style="stickyX">

    ...
</com.mufid.uiable.Draggable>
```
### GboardEditText
```xml
<com.mufid.uiable.GBoardEditText
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:layout_constraintTop_toTopOf="parent"
    app:layout_constraintStart_toStartOf="parent" 
    android:hint="Enter your message"/>
```
```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        ...
        val editText = findViewById<GBoardEditText>(R.id.edit_text)

        editText.setKeyBoardInputCallbackListener(object : GBoardEditText.KeyBoardInputCallbackListener {
            override fun onCommitContent(
                inputContentInfo: InputContentInfoCompat?,
                flags: Int,
                opts: Bundle?
            ) {
                // get content uri 
                val contentUri = inputContentInfo?.contentUri
            }
        })
    }
}
```

### All dependencies
```groovy
// build.gradle(project)
ext.uiable_version = "0.2.0"
...
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

// build.gradle(app)
implementation "com.github.imufiid:uiable:$uiable_version"

```

## License
```
Copyright 2021 Imam Mufiid

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
---