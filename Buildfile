
VERSION_NB = "0.0.1"

repositories.remote << "http://www.ibiblio.org/maven2/"

layout = Layout.new
layout[:source, :main, :java] = 'src'
layout[:source, :main, :resources] = 'assets'

define 'React', :layout => layout do
  compile.with Dir["libs/*.jar"]
  project.version = VERSION_NB
  run.using :main => "com.mentaldistortion.react.React"
  package :jar

  task :dox do
    Dir.chdir "doc" do
      system "doxygen"
    end
  end

end
