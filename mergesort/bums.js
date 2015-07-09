function less(x, y) {
  return x < y;
}

var callsToMerge = 0;

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

  callsToMerge += 1
  console.log(callsToMerge);
  console.log(a.join(' '));
}

function mergesort(a) {
  var N = a.length;
  var aux = new Array(N);
  for (var sz = 1; sz < N; sz = sz + sz) {
    for (var lo = 0; lo < N - sz; lo += sz + sz) {
      merge(a, aux, lo, lo + sz - 1, Math.min(lo + sz + sz - 1, N - 1));
    }
  }
}

var xs = '77 29 89 90 32 33 48 17 92 28';
var ys = xs.split(' ').map(function(x) { return parseInt(x, 10); });
console.log(ys);
mergesort(ys);
console.log(ys);

