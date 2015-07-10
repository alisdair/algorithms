function swap(a, i, j) {
  var tmp = a[i];
  a[i] = a[j]
  a[j] = tmp;
}

function partition(a, lo, hi) {
   if (hi <= lo)
     return;
   var lt = lo, gt = hi;
   var v = a[lo];
   var i = lo;
   while (i <= gt)
   {
      if      (a[i] < v) swap(a, lt++, i++);
      else if (a[i] > v) swap(a, i, gt--);
      else               i++;
   }
}

var xs = process.argv.slice(2);
console.log(xs);
var j = partition(xs, 0, xs.length - 1);
console.log(xs)
console.log(xs.join(' '))
