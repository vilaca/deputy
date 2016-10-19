# deputy
Invoke methods on remote Java classes over Http.


```

        // Pass 2 parameters to remove function using Http and test result
        
        Sum sum = (Sum) Deputy.useShared(Sum.class);

        Assert.assertEquals(7, sum.calc(3, 4));

```


