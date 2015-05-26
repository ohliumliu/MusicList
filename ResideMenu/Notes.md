# This is a collection of notes for MusicList-master

## activity_main.xml -- User interface for MainActivity.java
Each button is a textView with a drawable icon. Padding was used to position of the text with icon.
Clicking each button changes what is shown above the bottom panel.
Fragment is used above the panel.

`MainActivity` extends `BaseFragmentActivity` which is a `FragmentActivity`. `MainActivity` is required to
be extended from `FragmentActivity`. However, there are a collection of fragments used later.

`BaseFragmentActivity` also contains some common layout, such as title bar.

###`initView()`
This is the first step of the app. On a high level, it does the following

1. Obtain handles to various parts of the layout.
2. Setup listeners for the bottom panel.
3. Obtain handles to the fragments.

###`setCurrentTag()`
The first fragment is shown. It essentially calls `showFragment(Fragment frag, init tabIndex)`.

###`showFragment(Fragment frag, init tabIndex)`
It hides the current fragment, add frag to the container and update the index of current fragment.

Fragment is controlled by fragmentManger:

    getSupportFragmentManager()
    beginTransaction()
    add(fragment_container_layout, fragment, user-supplied-tag)


## Fragments design
Fragment <- BaseFragment <-

* ThemeFragment |??|TAB_ONE|
* MystyleFragment|????|TAB_TWO|
* UserFragment|??|TAB_THREE|

Life cycle related issues

* Setup the field variable to save the layout of fragment in `onCreateView()`
* Load the fragment layout in `onActivityCreated()` by `setContentView()`

    ### ThemeFragment.java and framgment_mytheme.xml

    * fragment_mytheme.xml has a viewPager for slideshow effect. In fact, it uses a custom class, FriendlyViewPager.
    * Read the tutorial to under how slideshow effect is implemented.
    * Understand Handler
    * Understand data IO

    ### MystyleFragment.java

    ### UserFragment.java

## Communication between fragment and calling activity

* Fragment side: define an interface which demands a callback. set the interface to be the calling activity in `onAttach()`.
* Call activity side: implements the interface defined in the fragment side.

# Version control
Use github.

* Local side: Need to install git and let Android Studio be aware of the git command (Setting -> Version Control -> Git)

    Choose git as the version control tool (Setting -> Version Control -> Change the setting in table)

* Remote side: Set up github account in Android Studio (Setting -> Version Control -> GitHub)
* Commit directory or file: right click -> Git -> Commit. This is a local commit.
* Push commit to GitHub. VCS -> Git -> Push