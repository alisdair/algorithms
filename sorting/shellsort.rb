class Shellsort
  def initialize(xs)
    @xs = xs
  end

  def stride!(h)
    i = h
    while i < xs.length
      j = i
      while j >= h && xs[j] < xs[j-h]
        swap!(j, j-h)
        j -= h
      end
      i += 1
    end
  end

  def swap!(i, j)
    tmp = @xs[i]
    @xs[i] = @xs[j]
    @xs[j] = tmp
  end

  def to_s
    @xs.to_s
  end

  def inspect
    @xs.inspect
  end

  attr_reader :xs
end
