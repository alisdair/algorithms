function swap(a, i, j) {
  var tmp = a[i];
  a[i] = a[j]
  a[j] = tmp;
}

function partition(a, lo, hi) {
  var i = lo, j = hi + 1;

  while (true) {
    while (a[++i] < a[lo])
      if (i == hi)
        break;

    while (a[lo] < a[--j])
      if (j == lo)
        break;

    if (i >= j)
      break;

    swap(a, i, j);
  }
  swap(a, lo, j);

  return j;
}

var xs = process.argv.slice(2);
console.log(xs);
var j = partition(xs, 0, xs.length - 1);
console.log('Element ' + j + ' (' + xs[j] + ') is in position');
console.log(xs)
console.log(xs.join(' '))
