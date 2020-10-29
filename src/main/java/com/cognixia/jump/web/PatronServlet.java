package com.cognixia.jump.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cognixia.jump.connection.ConnectionManager;
import com.cognixia.jump.dao.BookCheckoutDao;
import com.cognixia.jump.dao.BookCheckoutDaoImp;
import com.cognixia.jump.dao.BookDao;
import com.cognixia.jump.dao.BookDaoImp;
import com.cognixia.jump.dao.LibrarianDao;
import com.cognixia.jump.dao.LibrarianDaoImp;
import com.cognixia.jump.dao.PatronDao;
import com.cognixia.jump.dao.PatronDaoImp;
import com.cognixia.jump.model.Book;
import com.cognixia.jump.model.BookCheckout;
import com.cognixia.jump.model.Patron;

@WebServlet("/PatronServlet/*")
public class PatronServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private BookDao bookDao;
	private PatronDao patronDao;
	private BookCheckoutDao checkoutDao;
	private LibrarianDao librarianDao;
	private HttpSession session;

	@Override
    public void init() {
		bookDao = new BookDaoImp();
		patronDao = new PatronDaoImp();
		checkoutDao = new BookCheckoutDaoImp();
		librarianDao = new LibrarianDaoImp();
    }
	
	@Override
	public void destroy() {
		try {
			ConnectionManager.getConnection().close();
			session.invalidate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getPathInfo();
		if(action == null) {
			action = request.getServletPath();
		}
		System.out.println(action);
		switch(action) {
			case "/list":
				listBooks(request, response);
				break;
			case "/checkout":
				System.out.println("checkout");
				checkoutBook(request, response);
				break;
			case "/signup":
				signupPatron(request, response);
				break;
			default:
				goToPatronDashboard(request, response);
				break;
		}
	}
	
	private void goToPatronDashboard(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		session = request.getSession();
		
		if(session != null) {
			if(session.getAttribute("user") != null) {
				Patron pat = (Patron) session.getAttribute("user");
				
				List<BookCheckout> checkoutBooks = checkoutDao.getAllCurrentCheckoutsByPatronId(pat.getPatron_id());
				request.setAttribute("checkoutBooks", checkoutBooks);
				
//				List<Book> books = new ArrayList<>();
//				
//				for(BookCheckout bc: checkoutBooks) {
//					books.add(bookDao.getBookByISBN(bc.getIsbn()));
//				}
//				request.setAttribute("books", books);
				
				RequestDispatcher dispatch = request.getRequestDispatcher("/patronDashboard.jsp");
				dispatch.forward(request, response);	
			} else {
				response.sendRedirect("/LibraryCrudProject/AccessServlet/signinPage");
			}
		} else {
			response.sendRedirect("/LibraryCrudProject/AccessServlet/signinPage");
		}
	}
	
	private void listBooks(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("List Books - Patron");
		session = request.getSession();
		
		if(session != null) {
			if(session.getAttribute("user") != null) {
				List<Book> allBooks = bookDao.getAllBooks();
				request.setAttribute("allBooks", allBooks);
				RequestDispatcher dispatcher = request.getRequestDispatcher("/bookList.jsp");
				dispatcher.forward(request, response);
			} else {
				response.sendRedirect("/LibraryCrudProject/AccessServlet/signinPage");
			}
		} else {
			response.sendRedirect("/LibraryCrudProject/AccessServlet/signinPage");
		}
	}
	
	private void checkoutBook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
	
	private void returnBook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
	
	private void signupPatron(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
	
	private void signinPatron(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
	
	private void updatePatron(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
}