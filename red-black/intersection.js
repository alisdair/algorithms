var rb = require('./red-black')

function Line(name, x1, y1, x2, y2) {
  this.name = name;
  this.x1 = x1;
  this.y1 = y1;
  this.x2 = x2;
  this.y2 = y2;

  if (y1 === y2) {
    this.horizontal = true;
  }

  if (x1 === x2) {
    this.vertical = true;
  }

  if (!this.horizontal && !this.vertical) {
    throw new Error('No support for diagonals');
  }

  if (x1 > x2) {
    throw new Error('Expected x1 (' + x1 + ') <= x2 (' + x2 + ')');
  }

  if (y1 > y2) {
    throw new Error('Eypected y1 (' + y1 + ') <= y2 (' + y2 + ')');
  }
}

// Sample: "A ( 4,  0)  ->  (14,  0)  [ horizontal ]"
lineMatch = new RegExp(/^([A-Z]+) +\( *(\d+), *(\d+)\) *-> *\( *(\d+), *(\d+)\) *\[ (horizontal|vertical) *\]$/)
function parseLine(line) {
  var result = lineMatch.exec(line);
  if (result === null) {
    return;
  }

  var name = result[1];
  var x1 = +result[2];
  var y1 = +result[3];
  var x2 = +result[4];
  var y2 = +result[5];
  var kind = result[6];
  var line = new Line(name, x1, y1, x2, y2);

  if (kind === 'horizontal' && !line.horizontal) {
    throw new Error('Line "' + line + '": should be horizontal but is not');
  }

  if (kind === 'vertical' && !line.vertical) {
    throw new Error('Line "' + line + '": should be vertical but is not');
  }

  return line;
}

// Apparently reading lines from stdin is too hard for node.js?
var input = [
  'A ( 4,  0)  ->  (14,  0)  [ horizontal ]',
  'B ( 5, 15)  ->  (11, 15)  [ horizontal ]',
  'C (10, 12)  ->  (15, 12)  [ horizontal ]',
  'D ( 2,  7)  ->  ( 2, 18)  [ vertical   ]',
  'E ( 0,  1)  ->  (12,  1)  [ horizontal ]',
  'F ( 1, 10)  ->  ( 1, 16)  [ vertical   ]',
  'G ( 8, 14)  ->  (13, 14)  [ horizontal ]',
  'H ( 9,  8)  ->  ( 9,  9)  [ vertical   ]'
];

var lines = input.map(function(line) { return parseLine(line); });

function X(position, type, line) {
  this.position = position;
  this.type = type;
  this.line = line;
}

// This should be a binary heap but N is small so I don't care
var xs = [];
lines.forEach(function(line) {
  if (line.horizontal) {
    xs.push(new X(line.x1, 'h1', line));
    xs.push(new X(line.x2, 'h2', line));
  } else {
    xs.push(new X(line.x1, 'v', line));
  }
});
xs.sort(function(a, b) { return a.position - b.position; });

var tree = new rb.Tree();
var intersections = [];
function Intersection(vertical, horizontal) {
  this.vertical = vertical;
  this.horizontal = horizontal;
}

Intersection.prototype.toString = function() {
  return this.vertical.name + '-' + this.horizontal.name;
}

xs.forEach(function(x) {
  if (x.type === 'h1') {
    tree.put(x.line.y1, x.line);
  } else if (x.type === 'h2') {
    tree.remove(x.line.y1);
  } else if (x.type === 'v') {
    // if (x.line.name === 'H') {
    //   var lines = [];
    //   tree.traverse(function(node) {
    //     lines.push(node.value);
    //   });
    //   lines.sort(function(a, b) { return a.y1 - b.y1; });
    //   console.log(lines);
    // }
    tree.range(x.line.y1, x.line.y2).forEach(function(node) {
      intersections.push(new Intersection(x.line, node.value));
    });
  }
});

console.log('Intersections:', intersections);
