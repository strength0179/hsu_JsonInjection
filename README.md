# hsu_JsonInjection
 
When we got a json data and needed to set the data into UI, mostly we were doing 'TextView.setText' or 'ImageView.loadFromUrl (by Glide or Picaso).'

If there's 10 widget to set data, then there's 10-line of code to 'TextView.setText' or 'ImageView.loadFromUrl.'

It's like...
```
(TextView)findViewById(id1).setText(js.getString(key1))
(TextView)findViewById(id2).setText(js.getString(key2))
Glide.with(this).load(js.getString(key3)).into((ImageView)findViewById(id3));
```

This hsu_JsonInjection is extended from TextView to simplfy all those lines of code to 1 line : TextView.setText.

Example:
```
(hsu_JsonInjection)findViewById(id1).setText(js.toString());
```

Put hsu_JsonInjection in the XML Layout file. When this file were inflated or set as ContentView, hsu_JsonInjection would search for parentView to get evey child view in the parentView.

But hsu_JsonInjection can't tell which json-data would set as data to which widget. We need to edit the value of 'android:tag', so the hsu_JsomInjection will know what's the json data to set when it do the searching.

Example:
```
<TextView android:tag= "name"/>
```
hsu_JsonInjection will read the data with key:'name' and set it to this TextView.

Example:
```
<ImageView android:tag= "picUrl"/>
```
hsu_JsonInjection will read the data with key:'picUrl' and load it by URL to this ImageView.





# hsu_ObjectInjection

It's almost like hsu_JsonInject. The value of 'android:tag' now is the name of variable declared in the data class.

But we can't call 'TextView.setText' to set data. The reason is obvious.

We need to call 'ObjectInjector.setObject' instead.

Example:
```
(ObjectInjector)findViewById(id1).setObject(obj);
```
