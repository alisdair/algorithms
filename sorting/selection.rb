class Selection
  def initialize(xs)
    @xs = xs
    @i = 0
  end

  def iterate!
    min, j = @xs.each_with_index.reject {|_, i| i < @i }.min_by {|x, i| x }
    tmp = @xs[@i]
    @xs[@i] = min
    @xs[j] = tmp

    @i += 1
  end

  def to_s
    @xs.to_s
  end

  def inspect
    @xs.inspect
  end
end
