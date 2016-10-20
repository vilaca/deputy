# deputy
Invoke methods on remote Java classes over Http.


```

    // Create singleton object in remote machine
        
    Sum sum = (Sum) Deputy.useShared(Sum.class);


    // run method on remote machine and test result

    Assert.assertEquals(7, sum.calc(3, 4));

```


