
VERSION_NB = "0.0.1"

repositories.remote << "http://www.ibiblio.org/maven2/"

layout = Layout.new
layout[:source, :main, :java] = 'src'

define 'FyreWare', :layout => layout do
  compile.with Dir["lib/*.jar"]
  project.version = VERSION_NB
  run.using :main => "com.mentaldistortion.fyreware.FyreWareDesktop"
  package :jar
end
