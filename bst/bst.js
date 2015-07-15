function Node(key, value) {
  this.key = key;
  this.value = value;
  this.left = null;
  this.right = null;
}

function Tree() {
  this.root = null;
}

Tree.prototype.put = function(key, value) {
  function put(node, key, value) {
    if (!node) {
      return new Node(key, value);
    }

    if (key < node.key) {
      node.left = put(node.left, key, value);
    } else if (key > node.key) {
      node.right = put(node.right, key, value);
    } else {
      node.value = value;
    }

    return node;
  }

  this.root = put(this.root, key, value);
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

var tree = new Tree();
var logKeys = function() {
  var keys = [];
  tree.traverse(function(node) { keys.push(node.key); });
  console.log(keys.join(' '));
}

var nodes = process.argv.slice(2);
for (var i = 0; i < nodes.length; i++) {
  tree.put(parseInt(nodes[i], 10), i);
}
logKeys();
