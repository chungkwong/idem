/*
 * Copyright (C) 2015 kwong
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
import com.github.chungkwong.idem.lib.lang.prolog.*;
import java.util.*;
import org.junit.*;

/**
 *
 * @author kwong
 */
public class PrologTest{
	static Database db=new Database();
	public PrologTest(){
	}
	@BeforeClass
	public static void setUpClass(){
		//@see http://www.learnprolognow.org/lpnpage.php?pagetype=html&pageid=lpn-htmlse1
		db.addClause(new Clause(new CompoundTerm("woman",Collections.singletonList(new Atom("mia"))),new Atom("true")));
		db.addClause(new Clause(new CompoundTerm("woman",Collections.singletonList(new Atom("jody"))),new Atom("true")));
		db.addClause(new Clause(new CompoundTerm("woman",Collections.singletonList(new Atom("yolanda"))),new Atom("true")));
		db.addClause(new Clause(new CompoundTerm("playAirGuitar",Collections.singletonList(new Atom("jody"))),new Atom("true")));
		db.addClause(new Clause(new Atom("party"),new Atom("true")));
	}
	@AfterClass
	public static void tearDownClass(){
	}

	@Before
	public void setUp(){
	}

	@After
	public void tearDown(){
	}
	Substitution query(Predication goal,Database db){
		return new Processor(goal,db).getSubstitution();
	}
	List<Substitution> multiquery(Predication goal,Database db){
		List<Substitution> substs=new ArrayList<>();
		Processor processor=new Processor(goal,db);
		while(processor.getSubstitution()!=null){
			substs.add(processor.getSubstitution());
			processor.reexecute();
		}
		return substs;
	}
	@Test public void testPositiveAtom(){
		Assert.assertNotNull(query(new Atom("party"),db));
	}
	@Test public void testNegativeAtom(){
		Assert.assertNull(query(new Atom("rockConcert"),db));
	}
	@Test public void testSimpleCompound(){
		Assert.assertNotNull(query(new CompoundTerm("woman",Collections.singletonList(new Atom("mia"))),db));
		Assert.assertNotNull(query(new CompoundTerm("playAirGuitar",Collections.singletonList(new Atom("jody"))),db));
		Assert.assertNull(query(new CompoundTerm("playAirGuitar",Collections.singletonList(new Atom("mia"))),db));
	}
	@Test public void testMulti(){
		Assert.assertTrue(multiquery(new CompoundTerm("woman",Collections.singletonList(new Variable("X"))),db).size()==3);
	}
	@Test public void testNonexist(){
		Assert.assertNull(query(new CompoundTerm("tatooed",Collections.singletonList(new Atom("jody"))),db));
	}
}
