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

TODO:
1. 协程版简易的 EventBus 工具
2. MMKV 的持久化存储工具
3. 以及各种我想到的一些基础控件

