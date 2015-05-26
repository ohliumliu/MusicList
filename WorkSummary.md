# 20150523

* Draft of fragment_user.xml based on Xie Peng's design.
    - fragement_user.xml is modified
    - add `clear_cach_data.xml`
    - add `clear_download_data.xml`
    - use like and unlike button as a place holder for icons
    - need to implement data size update, toggle switch listener, onClickListener for each item.
* Update top title when switching tabs.
    - In `MainActivity.java`, implement title updating codes in method `setCurrentTag(int tagIndex)`. Not sure if this is the right place to do this.
    - add methods in `ThemeFragment.java`
        1. `public String getCurrentTitle()`. This is not used yet.
        2. `public String getCurrentType()`. This method returns the "type" of current song.
        In class `ThemeFragment`, one has
      	`private FriendlyViewpager mViewPager;` and
        `private ArrayList<ThemesList> themesSummaries;`

        mViewPager contains the ID of the song being played. Each element of themesSummaries is a ThemesList,
        corresponding to a particular song. ThemesList further contains `targetObject` which
        is `public HashMap<Integer, Themesitems>`. Themesitems contains the "type" of the song, among other things.


        `ThemesList currentThemesList =  themesSummaries.get(mViewPager.getCurrentItem());
         int currentTypeId = currentThemesList.targetObject.get(currentThemesList.getTargetId()).getType();
         return PageListener.typedef.values()[currentTypeId].toString();`