package com.iutbm.monumentdroid.adapters;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iutbm.monumentdroid.activity.R;
import com.iutbm.monumentdroid.models.Comment;
import com.iutbm.monumentdroid.models.User;

public class AdapterListCommentaires extends BaseAdapter {

	private ArrayList<Comment> commentaires;
	private LayoutInflater myInflater;

	
	/**
	 * Constructeur par défaut
	 * @param _context
	 * @param _commentaires
	 */
	public AdapterListCommentaires (Context _context, ArrayList<Comment> _commentaires)
	{
		this.myInflater = LayoutInflater.from(_context);
		this.commentaires = _commentaires;
	}
	

	/**
	 * Retourne le nombre de commentaires
	 */
	public int getCount() {
		return this.commentaires.size();
	}

	/**
	 * Retourne le commentaires à l'indice position de la listView
	 * @param position
	 */
	public Comment getItem(int position) {
		return this.commentaires.get(position);
	}

	/**
	 * Retourne le numéro du commentaires à la position position
	 */
	public long getItemId(int position) {
		return this.commentaires.get(position).getId();
	}

	/**
	 *  Classe permettant d'afficher des information sur une les vues de la liste
	 * @author julienpetit
	 *
	 */
	public static class ViewHolder {
		TextView nom;
		TextView date;
		TextView commentaire;
	}
	
	/**
	 * Retourne une Vue 
	 */
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		// Parseur de la date de long à texte 
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		
		ViewHolder holder;
		
		if (convertView == null)
		{
			convertView = myInflater.inflate(R.layout.itemlistecommentaires, null);
			holder = new ViewHolder();
			
			
			holder.nom = (TextView) convertView.findViewById(R.itemlistecommentaires.nomUser);
			holder.commentaire = (TextView) convertView.findViewById(R.itemlistecommentaires.commentaire);
			holder.date = (TextView) convertView.findViewById(R.itemlistecommentaires.date);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// affichage du texte dans les vues
		User user = commentaires.get(position).getUser();
		
		holder.nom.setText(user.getLogin());
		holder.commentaire.setText(commentaires.get(position).getMessage());
		holder.date.setText(format.format(new Date(commentaires.get(position).getDate().getTime())));
		
		return convertView;
	}
}
