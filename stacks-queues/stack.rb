class ExerciseStack
  attr_reader :stack

  def initialize
    @pushes = (0..10).to_a
    @stack = []
  end

  def self.execute(commands)
    stack = ExerciseStack.new
    commands.each {|c| stack.send(c) }
    print "\n"
    puts "(#{stack.stack})"
  end

  def pop
    print "#{@stack.pop} "
  end

  def push
    @stack.push @pushes.shift
  end
end
