class Queue
  attr_reader :queue

  def initialize
    @inputs = (0..10).to_a
    @queue = []
  end

  def self.execute(commands)
    queue = Queue.new
    commands.each {|c| queue.send(c) }
    print "\n"
    puts "(#{queue.queue})"
  end

  def d
    print "#{@queue.shift} "
  end

  def q
    @queue.push @inputs.shift
  end
end

def x(s)
  Queue.execute(s.split)
end
