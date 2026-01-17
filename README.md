# 描述

这是我自用的一个工具类库，包含了一些自己想到的工具和自定义 View，同时很多也借鉴了 github 上许多其他人的思想，
如果我能找到的我会在我的代码注明来源，没有找到的您若是发现了您的代码身影可以联系我，我添加出处。

# 注意事项

本人十分喜欢开源精神，这个库就是我自身的一些想法和从他人代码中学习到的编程思想的实现，我尊重任何一个开源作者，如果有涉及到违反了您开源协议的情况，
请通知我，因为有些代码我只是有印象就写上去了，我会根据您的开源协议修改一些东西，谢谢。

[![](https://jitpack.io/v/Shadowmirror/MiaoLib.svg)](https://jitpack.io/#Shadowmirror/MiaoLib)
本库接入了 JitPack 自动发包，我一般是有一堆东西更新的时候才会发包，如果您想导入包而不是直接 Copy 我的代码，可以通知我

接入代码

```groovy
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

```groovy
dependencies {
    implementation 'com.github.Shadowmirror.MiaoLib:MiaoLib:{newVersion}'
}
```

# 功能介绍：

## ViewDialog

### 实现背景：

在我的开发过程中，我用过的 Dialog 的方案不多，也就是自定义 DialogFragment，XPopup 以及 Compose 原生 Dialog 的方案，其中最好用的是 XPopup 的方案。
XPopup 基本上能满足我 90 % 的需求，但是由于其不怎么更新了，我自己也想写一个简单的 Dialog 框架就一时兴起实现了。

### 功能：

1. 直接继承 Activity 的导航栏和状态栏状态（由于是 View 实现的 Dialog 方案，
   所以不会在像传统 Dialog / DialogFragment / PopupWindow 或者通过 context 获取 DecorView 对象实现 addView 这几种方案一样无法直接继承状态栏和导航栏的状态,
   都需要自己去处理状态栏和导航栏的显示隐藏，导致有些机型会有状态栏和导航栏显示隐藏的动画，会有点丑丑的。）
2. 可自定义动画 & 内置动画([DialogAnimator.kt](miaoLibrary/src/main/java/miao/kmirror/miaolibrary/ui/DialogAnimator.kt))
   （这里是因为有些时候有些弹窗的进场动画会和内部原生联动，所以就需要自己写动画了，展示的时候 DialogAnimator 改成 NoAnimation 即可）
3. 支持 Compose & XML 方案，其实我自己现在都主要用 Compose 了 XML 只是为了部分老页面的使用
4. 生命周期绑定，其实比较简单就是在 Activity onDestroy 的时候 removeView 就行了

### 注意：

1. 如果你的应用没有全屏应用以及不允许弹窗的时候系统栏动画的显示隐藏，我建议使用 DialogFragment / XPopup，我这个就是简单的 View Dialog 而已，可能会有些我没见到过的场景我没有顾虑到。
2. 没有状态保存，我写项目现在都是 MVVM 的，数据都放在 ViewModel 层，所以不用关心这个。

### TODO:

1. 弹窗管理(弹窗队列，优先级)
2. 等等（我想到了再写）

### 用法：请参考我 App 的用法

