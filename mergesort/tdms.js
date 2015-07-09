function less(x, y) {
  return x < y;
}

function merge(a, aux, lo, mid, hi) {
  for (var k = lo; k <= hi; k++) {
    aux[k] = a[k];
  }

  var i = lo, j = mid + 1;

  for (var k = lo; k <= hi; k++) {
    if (i > mid) {
      a[k] = aux[j++];
    } else if (j > hi) {
      a[k] = aux[i++];
    } else if (less(aux[j], aux[i])) {
      a[k] = aux[j++];
    } else {
      a[k] = aux[i++];
    }
  }
}

var callsToMerge = 0;

function sort(a, aux, lo, hi) {
  if (hi <= lo) {
    return;
  }

  var mid = Math.floor(lo + (hi - lo) / 2);
  sort(a, aux, lo, mid);
  sort(a, aux, mid + 1, hi);
  merge(a, aux, lo, mid, hi);
  callsToMerge += 1;
  console.log(callsToMerge);
  console.log(a.join(' '));
}

function mergesort(a) {
  var aux = new Array(a.length);
  sort(a, aux, 0, a.length - 1);
}

var xs = '81 26 58 54 99 49 98 94 64 25 18 45';
var ys = xs.split(' ').map(function(x) { return parseInt(x, 10); });
console.log(ys);
mergesort(ys);
console.log(ys);
