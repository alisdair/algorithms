var RED = true;
var BLACK = false;

function Node(key, value, color) {
  this.key = key;
  this.value = value;
  this.left = null;
  this.right = null;
  this.color = color;
}

function Tree() {
  this.root = null;
}

Tree.prototype.put = function(key, value) {
  function isRed(x) {
    if (x === null) {
      return false;
    }

    return x.color === RED;
  }

  function rotateLeft(h) {
    if (!isRed(h.right)) { throw "Can't rotate left unless right node is red"; }

    var x = h.right;

    h.right = x.left;
    x.left = h;
    x.color = h.color;
    h.color = RED;

    return x;
  }

  function rotateRight(h) {
    if (!isRed(h.left)) { throw "Can't rotate right unless left node is red"; }

    var x = h.left;

    h.left = x.right;
    x.right = h;
    x.color = h.color;
    h.color = RED;

    return x;
  }

  function flipColors(h) {
    if (isRed(h)) { throw "Can't flip node which is red"; }
    if (!isRed(h.left)) { throw "Can't flip unless left node is red"; }
    if (!isRed(h.right)) { throw "Can't flip unless right node is red"; }

    h.color = RED;
    h.left.color = BLACK;
    h.right.color = BLACK;
  }

  function put(node, key, value) {
    if (!node) {
      return new Node(key, value, RED);
    }

    if (key < node.key) {
      node.left = put(node.left, key, value);
    } else if (key > node.key) {
      node.right = put(node.right, key, value);
    } else {
      node.value = value;
    }

    if (isRed(node.right) && !isRed(node.left)) {
      node = rotateLeft(node);
    }
    if (isRed(node.left) && isRed(node.left.left)) {
      node = rotateRight(node);
    }
    if (isRed(node.left) && isRed(node.right)) {
      flipColors(node);
    }

    return node;
  }

  this.root = put(this.root, key, value);
  this.root.color = BLACK;
}

Tree.prototype.height = function() {
  function height(node) {
    if (!node) {
      return 0;
    }

    return Math.max(height(node.left), height(node.right)) + 1;
  }

  return height(this.root);
}

Tree.prototype.traverse = function(f) {
  var queue = [this.root];

  while (queue.length > 0) {
    var node = queue.shift();

    f(node);

    if (node.left) {
      queue.push(node.left);
    }

    if (node.right) {
      queue.push(node.right);
    }
  }
}

Tree.prototype.remove = function(key) {
  function min(node) {
    if (!node) {
      return null;
    }

    if (!node.left) {
      return node;
    }

    return min(node.left);
  }

  function removeMin(node) {
    if (!node.left) {
      return node.right;
    }
    node.left = removeMin(node.left);
    return node;
  }

  function remove(node, key) {
    if (!node) {
      return null;
    }

    if (key < node.key) {
      node.left = remove(node.left, key);
    } else if (key > node.key) {
      node.right = remove(node.right, key);
    } else {
      if (!node.right) {
        return node.left;
      }
      if (!node.left) {
        return node.right;
      }

      var t = node;
      node = min(t.right);
      node.right = removeMin(t.right);
      node.left = t.left;
    }

    return node;
  }

  this.root = remove(this.root, key);
}

Tree.prototype.range = function(lo, hi) {
  function range(node, results, lo, hi) {
    if (node == null) {
      return;
    }

    if (lo < node.key) {
      range(node.left, results, lo, hi);
    }

    if (lo <= node.key && hi >= node.key) {
      results.push(node);
    }

    if (hi > node.key) {
      range(node.right, results, lo, hi);
    }
  }

  var results = [];
  range(this.root, results, lo, hi);
  return results;
}

function findKeys(tree) {
  var keys = [];
  tree.traverse(function(node) { keys.push(node.key); });
  return keys;
}

function findReds(tree) {
  var nodes = [];
  tree.traverse(function(node) {
    if (node.color === RED) {
      nodes.push(node.key);
    }
  });
  return nodes.sort();
}

function buildTree(nodes) {
  var tree = new Tree();

  for (var i = 0; i < nodes.length; i++) {
    tree.put(nodes[i], i);
  }

  return tree;
}

// var nodes = process.argv.slice(2);
// if (nodes.length === 0) {
//   nodes = 'S E A R C H X M P L'.split(' '); // => M E R C L P X A H S
// }

// var tree = buildTree(nodes);
// console.log(findKeys(tree).join(' '))

module.exports = {
  Tree: Tree,
  buildTree: buildTree,
  findKeys: findKeys
};
